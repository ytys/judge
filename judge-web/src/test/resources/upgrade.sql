use clanguage;

create database if not exists oj_temp_schema COLLATE 'utf8_general_ci';

drop table if exists oj_temp_schema.userprofile_clanguage;
create table oj_temp_schema.userprofile_clanguage
    select
        null            as      birth_date,
        reg_time        as      creation_date,
        IF(defunct='Y',1,0)  as disabled,
        email           as      email,
        user_id         as      handle,
        accesstime      as      last_update_date,
        school          as      major,
        nick            as      nickname,
        password        as      password,
        school          as      school,
        NULL            as      creation_user
    from users u1
    order by creation_date asc, handle asc;

use oj_temp_schema;

update userprofile_clanguage set
        email = replace(trim(email),' ',''),
        nickname = trim(nickname),
        school = trim(school),
        major = trim(major);

update userprofile_clanguage set email = null where email = '' or email='null' or length(email) < 2;

ALTER TABLE `userprofile_clanguage`
    ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT FIRST,
    ADD PRIMARY KEY (`id`);

ALTER TABLE `userprofile_clanguage`
    ADD INDEX `KEY_email` (`email`);

UPDATE
    userprofile_clanguage AS t
    LEFT JOIN (
        select tb.id, tb.email, count(rt.id) as cnt
        from userprofile_clanguage tb, userprofile_clanguage rt
        where rt.id < tb.id and rt.email = tb.email
        group by tb.id
        order by cnt desc
    ) as m on
        m.id = t.id
set
    t.email = CONCAT('$', m.cnt, '$', t.email)
where
    m.cnt is not null and
    t.email not like '$%';

ALTER TABLE `userprofile_clanguage`
	DROP COLUMN `id`;

insert into oj.userprofile (
    birth_date,
    creation_date,
    disabled,
    email,
    handle,
    last_update_date,
    major,
    nickname,
    password,
    school,
    creation_user
) select
    birth_date,
    if(creation_date is null, if(last_update_date is null,now(),last_update_date), creation_date),
    disabled,
    email,
    handle,
    last_update_date,
    major,
    nickname,
    password,
    school,
    creation_user
	from oj_temp_schema.userprofile_clanguage;


use java;
drop table if exists oj_temp_schema.userprofile_java;

create table oj_temp_schema.userprofile_java
    select
        cast(null as int) as id,
        u1.user_id      as      handle,
        u1.password     as      password,
        u1.nick         as      nickname,
        u1.email        as      email,
        ''              as      first_name,
        ''              as      last_name,
        ip              as      last_login_ip,
        accesstime      as      last_access_time,
        school          as      major,
        NULL            as      creation_user,
        u1.reg_time     as      creation_date,
        IF(u1.defunct='Y',1,0)  as disabled
    from users u1
    order by creation_date asc, handle asc;

use oj_temp_schema;

create table oj_temp_schema.userprofile
    select * from oj_temp_schema.userprofile_clanguage union
    select * from oj_temp_schema.userprofile_java;

drop table oj_temp_schema.userprofile_clanguage;
drop table oj_temp_schema.userprofile_java;

/* select * from userprofile tb where (select count(1) from userprofile where handle=tb.handle)>=2; */

/*
update users set email = null where length(email) <= 4;

update users set submit = (
    select count(*) from solution where solution.user_id = users.user_id
);

select * from users tb where (
	select count(1) from users where email = tb.email
) >= 2 and email is not null and trim(email) <>'' and email<>'null' order by email asc;

*/
