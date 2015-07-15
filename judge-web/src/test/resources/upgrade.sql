use oj;
ALTER TABLE access_log
    DROP FOREIGN KEY IF EXISTS FK_access_log_userprofile;
ALTER TABLE access_log
    ADD CONSTRAINT FK_access_log_userprofile FOREIGN KEY (userprofile) REFERENCES userprofile (id) ON UPDATE CASCADE;

ALTER TABLE contest
    DROP FOREIGN KEY IF EXISTS FK_contest_creation_user,
    DROP FOREIGN KEY IF EXISTS FK_contest_last_update_user;
ALTER TABLE contest
    ADD CONSTRAINT FK_contest_creation_user FOREIGN KEY (creation_user) REFERENCES userprofile (id) ON UPDATE CASCADE,
    ADD CONSTRAINT FK_contest_last_update_user FOREIGN KEY (last_update_user) REFERENCES userprofile (id);

ALTER TABLE contest_userprofile_statistics
    DROP FOREIGN KEY IF EXISTS FK_contest_userprofile_statistics_contest,
    DROP FOREIGN KEY IF EXISTS FK_contest_userprofile_statistics_userprofile;
ALTER TABLE contest_userprofile_statistics
    ADD CONSTRAINT FK_contest_userprofile_statistics_contest FOREIGN KEY (contest) REFERENCES contest (id) ON UPDATE CASCADE,
    ADD CONSTRAINT FK_contest_userprofile_statistics_userprofile FOREIGN KEY (userprofile) REFERENCES userprofile (id) ON UPDATE CASCADE;

ALTER TABLE judge_reply
    DROP FOREIGN KEY IF EXISTS FK_judge_reply_creation_user,
    DROP FOREIGN KEY IF EXISTS FK_judge_reply_last_update_user;
ALTER TABLE judge_reply
    ADD CONSTRAINT FK_judge_reply_creation_user FOREIGN KEY (creation_user) REFERENCES userprofile (id) ON UPDATE CASCADE,
    ADD CONSTRAINT FK_judge_reply_last_update_user FOREIGN KEY (last_update_user) REFERENCES userprofile (id) ON UPDATE CASCADE;

ALTER TABLE language
    DROP FOREIGN KEY IF EXISTS FK_language_creation_user;
ALTER TABLE language
    ADD CONSTRAINT FK_language_creation_user FOREIGN KEY (creation_user) REFERENCES userprofile (id) ON UPDATE CASCADE;

ALTER TABLE problem
    DROP FOREIGN KEY IF EXISTS FK_problem_creation_user,
    DROP FOREIGN KEY IF EXISTS FK_problem_limits;
ALTER TABLE problem
    ADD CONSTRAINT FK_problem_creation_user FOREIGN KEY (creation_user) REFERENCES userprofile (id) ON UPDATE CASCADE,
    ADD CONSTRAINT FK_problem_limits FOREIGN KEY (limits) REFERENCES limits (id) ON UPDATE CASCADE;

ALTER TABLE submission
    DROP FOREIGN KEY IF EXISTS FK_submission_contest,
    DROP FOREIGN KEY IF EXISTS FK_submission_judge_reply,
    DROP FOREIGN KEY IF EXISTS FK_submission_language,
    DROP FOREIGN KEY IF EXISTS FK_submission_problem,
    DROP FOREIGN KEY IF EXISTS FK_submission_userprofile;
ALTER TABLE submission
    ADD CONSTRAINT FK_submission_contest FOREIGN KEY (contest) REFERENCES contest (id) ON UPDATE CASCADE,
    ADD CONSTRAINT FK_submission_judge_reply FOREIGN KEY (judge_reply) REFERENCES judge_reply (id) ON UPDATE CASCADE,
    ADD CONSTRAINT FK_submission_language FOREIGN KEY (language) REFERENCES language (id) ON UPDATE CASCADE,
    ADD CONSTRAINT FK_submission_problem FOREIGN KEY (problem) REFERENCES problem (id) ON UPDATE CASCADE,
    ADD CONSTRAINT FK_submission_userprofile FOREIGN KEY (userprofile) REFERENCES userprofile (id);

ALTER TABLE usergroup
    DROP FOREIGN KEY IF EXISTS FK_usergroup_owner;
ALTER TABLE usergroup
    ADD CONSTRAINT FK_usergroup_owner FOREIGN KEY (owner) REFERENCES userprofile (id) ON UPDATE CASCADE;

ALTER TABLE usergroup_userprofile
    DROP FOREIGN KEY IF EXISTS FK_usergroup_userprofile_usergroup,
    DROP FOREIGN KEY IF EXISTS FK_usergroup_userprofile_userprofile;
ALTER TABLE usergroup_userprofile
    ADD CONSTRAINT FK_usergroup_userprofile_usergroup FOREIGN KEY (usergroup) REFERENCES usergroup (id) ON UPDATE CASCADE,
    ADD CONSTRAINT FK_usergroup_userprofile_userprofile FOREIGN KEY (userprofile) REFERENCES userprofile (id) ON UPDATE CASCADE;

ALTER TABLE userprofile
    DROP FOREIGN KEY IF EXISTS FK_userprofile_creation_user;
ALTER TABLE userprofile
    ADD CONSTRAINT FK_userprofile_creation_user FOREIGN KEY (creation_user) REFERENCES userprofile (id);

ALTER TABLE userprofile_role
    DROP FOREIGN KEY IF EXISTS FK_userprofile_role_role,
    DROP FOREIGN KEY IF EXISTS FK_userprofile_role_userprofile;
ALTER TABLE userprofile_role
    ADD CONSTRAINT FK_userprofile_role_role FOREIGN KEY (role) REFERENCES role (id) ON UPDATE CASCADE,
    ADD CONSTRAINT FK_userprofile_role_userprofile FOREIGN KEY (userprofile) REFERENCES userprofile (id) ON UPDATE CASCADE;



use clanguage;

create database if not exists oj_temp_schema COLLATE 'utf8_general_ci';

/* userprofile start */
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
    from clanguage.users u1
    order by creation_date asc, handle asc;
/* userprofile end */
/* problem start */
drop table if exists oj_temp_schema.problem_clanguage;
create table oj_temp_schema.problem_clanguage
    select
        problem_id as orign_id,
        title,
        description,
        input,
        output,
        sample_input,
        sample_output,
        hint,
        source,
        sample_Program,
        in_date as creation_date,
        time_limit,
        memory_limit,
        case_time_limit,
        CAST(NULL AS INT) AS new_id
    from clanguage.problem p
    where p.defunct='N';
ALTER TABLE oj_temp_schema.problem_clanguage
    CHANGE COLUMN new_id new_id BIGINT NULL DEFAULT NULL AFTER case_time_limit;

/* problem end */


use oj_temp_schema;

update oj_temp_schema.userprofile_clanguage
set
    email = replace(trim(email),' ',''),
    nickname = trim(nickname),
    school = trim(school),
    major = trim(major);

update oj_temp_schema.userprofile_clanguage set email = null where email = '' or email='null' or length(email) < 2;

ALTER TABLE oj_temp_schema.userprofile_clanguage
    ADD COLUMN id INT NOT NULL AUTO_INCREMENT FIRST,
    ADD PRIMARY KEY (id);

ALTER TABLE oj_temp_schema.userprofile_clanguage
    ADD INDEX KEY_email (email);

UPDATE
    oj_temp_schema.userprofile_clanguage AS t
    LEFT JOIN (
        select tb.id, tb.email, count(rt.id) as cnt
        from oj_temp_schema.userprofile_clanguage tb, oj_temp_schema.userprofile_clanguage rt
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

ALTER TABLE oj_temp_schema.userprofile_clanguage
    DROP COLUMN id;
delete from oj_temp_schema.userprofile_clanguage
    where handle in('admin', 'anonymousUser', 'system');
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

ALTER TABLE oj.problem AUTO_INCREMENT=0;
insert into oj.problem (
    id,
    creation_date,
    description,
    hint,
    input,
    last_update_date,
    output,
    sample_input,
    sample_output,
    source,
    title,
    creation_user,
    limits
) select
    orign_id,
    creation_date,
    description,
    hint,
    input,
    creation_date,
    output,
    sample_input,
    sample_output,
    source,
    title,
    NULL,
    1
from
    oj_temp_schema.problem_clanguage;

use clanguage;
drop table if exists oj_temp_schema.contest_clanguage;
create table oj_temp_schema.contest_clanguage
select
    contest_id,
    title,
    start_time,
    end_time,
    description,
    IF(defunct='N',0,1) as disabled
from clanguage.contest;

insert into oj.contest (
    id,
    begin_time,
    creation_date,
    disabled,
    finish_time,
    last_update_date,
    name,
    password,
    title,
    type,
    creation_user,
    last_update_user,
    description,
    parent
) select
    contest_id,
    start_time,
    now(),
    disabled,
    end_time,
    now(),
    contest_id,
    NULL,
    title,
    2, /* CONTEST */
    1, /* system user */
    1, /* system user */
    description,
    NULL
from
    oj_temp_schema.contest_clanguage;

use oj_temp_schema;
drop table if exists oj_temp_schema.solution_clanguage;
create table oj_temp_schema.solution_clanguage
select
    solution_id,
    problem_id,
    user_id,
    time,
    memory,
    in_date,
    result,
    language,
    ip,
    contest_id,
    code_length
from clanguage.solution;

ALTER TABLE oj_temp_schema.solution_clanguage
    ADD INDEX solution_id (solution_id);

ALTER TABLE oj_temp_schema.solution_clanguage
    ADD COLUMN source_code LONGTEXT NULL AFTER code_length,
    ADD COLUMN compile_info LONGTEXT NULL AFTER source_code;

SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO';
update
    oj_temp_schema.solution_clanguage as t
    left join (
        select
            solution_id as id,
            convert(uncompress(source_code.source) using 'utf8') AS source
        from clanguage.source_code
    ) as s on
        t.solution_id = s.id
set t.source_code = s.source;
update
    oj_temp_schema.solution_clanguage as t
    left join (
        select
            solution_id as id,
            convert(uncompress(source_code.source) using 'latin1') AS source
        from clanguage.source_code
    ) as s on
        t.solution_id = s.id
   left join (
   	select
            solution_id as id,
            code_length as len
   	from clanguage.solution
   ) as r on
   	r.id = s.id
set t.source_code = s.source
where t.code_length <> char_length(t.source_code);
SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '');

/*
select * from solution_clanguage
where char_length(source_code) <> code_length;
*/
/*
select * from oj_temp_schema.solution_clanguage
	where instr(source_code,'??')>0
	order by solution_id desc;
*/



/*
use oj_temp_schema;

create table oj_temp_schema.userprofile
    select * from oj_temp_schema.userprofile_clanguage union
    select * from oj_temp_schema.userprofile_java;

drop table oj_temp_schema.userprofile_clanguage;
drop table oj_temp_schema.userprofile_java;
*/
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
