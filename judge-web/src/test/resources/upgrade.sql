use clanguage;

create database if not exists oj_temp_schema COLLATE 'utf8_general_ci';

CREATE TABLE IF NOT EXISTS oj_temp_schema.contest_temp (
  orign_oj varchar(255) NOT NULL,
  orign_id bigint(20) NOT NULL,
  title longtext,
  start_time datetime DEFAULT NULL,
  end_time datetime DEFAULT NULL,
  description longtext,
  disabled bit(1) NOT NULL DEFAULT b'0',
  new_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (orign_oj,orign_id)
);

CREATE TABLE IF NOT EXISTS oj_temp_schema.problem_temp (
  orign_oj varchar(255) NOT NULL,
  orign_id bigint(20) NOT NULL,
  title varchar(255) DEFAULT NULL,
  description longtext,
  input longtext,
  output longtext,
  sample_input longtext,
  sample_output longtext,
  hint longtext,
  source varchar(100) DEFAULT NULL,
  sample_Program varchar(255) DEFAULT NULL,
  creation_date datetime DEFAULT NULL,
  time_limit int(11) DEFAULT NULL,
  memory_limit int(11) DEFAULT NULL,
  case_time_limit int(11) DEFAULT NULL,
  disabled bit(1) DEFAULT b'0',
  new_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (orign_oj,orign_id)
);

CREATE TABLE IF NOT EXISTS oj_temp_schema.solution_temp (
  orign_oj varchar(255) NOT NULL,
  orign_id bigint(20) NOT NULL,
  orign_problem_id bigint(11) NOT NULL,
  orign_user_handle varchar(255) NOT NULL DEFAULT '',
  orign_contest_id bigint(20) DEFAULT NULL,
  time int(11) NOT NULL DEFAULT '0',
  memory int(11) NOT NULL DEFAULT '0',
  in_date datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  result smallint(6) NOT NULL DEFAULT '0',
  language tinyint(4) NOT NULL DEFAULT '0',
  ip varchar(32) NOT NULL DEFAULT '',
  code_length int(11) NOT NULL DEFAULT '0',
  source_code longtext,
  compile_info longtext,
  PRIMARY KEY (orign_oj,orign_id)
);

CREATE TABLE IF NOT EXISTS oj_temp_schema.userprofile_temp (
  orign_oj varchar(255) NOT NULL,
  handle varchar(255) NOT NULL,
  birth_date datetime DEFAULT NULL,
  creation_date datetime DEFAULT NULL,
  disabled bit(1) NOT NULL DEFAULT b'0',
  email longtext,
  last_update_date datetime DEFAULT NULL,
  major longtext,
  nickname longtext,
  password longtext,
  school longtext,
  PRIMARY KEY (orign_oj,handle),
  KEY KEY_email (email(100)) USING HASH
);

set @oj_name = DATABASE();
/* userprofile start */
INSERT IGNORE INTO oj_temp_schema.userprofile_temp (
    orign_oj,
    handle,
    creation_date,
    disabled,
    email,
    last_update_date,
    nickname,
    password,
    school
) select
    @oj_name,
    user_id,
    reg_time,
    IF(defunct='Y',1,0),
    replace(trim(email),' ',''),
    accesstime,
    trim(nick),
    password,
    trim(school)
from
    users u1;
/* userprofile end */
/* problem start */
INSERT IGNORE INTO oj_temp_schema.problem_temp (
    orign_oj,
    orign_id,
    title,
    description,
    input,
    output,
    sample_input,
    sample_output,
    hint,
    source,
    sample_Program,
    creation_date,
    time_limit,
    memory_limit,
    case_time_limit,
    disabled
) select
    @oj_name,
    problem_id,
    title,
    description,
    input,
    output,
    sample_input,
    sample_output,
    hint,
    source,
    sample_Program,
    in_date,
    time_limit,
    memory_limit,
    case_time_limit,
    IF(defunct='N' or defunct is null,0,1)
from
    problem p;
/* problem end */
/* contest start */
INSERT IGNORE INTO oj_temp_schema.contest_temp (
    orign_oj,
    orign_id,
    title,
    start_time,
    end_time,
    description,
    disabled
) select
    @oj_name,
    contest_id,
    title,
    start_time,
    end_time,
    description,
    IF(defunct='N',0,1)
from contest;
/* contest end */

/* solution start */
INSERT IGNORE INTO oj_temp_schema.solution_temp (
    orign_oj,
    orign_id,
    orign_problem_id,
    orign_user_handle,
    orign_contest_id,
    time,
    memory,
    in_date,
    result,
    language,
    ip,
    code_length
) select
    @oj_name,
    solution_id,
    problem_id,
    user_id,
    contest_id,
    time,
    memory,
    in_date,
    result,
    language,
    ip,
    code_length
from solution;
/* solution end */

update oj_temp_schema.userprofile_temp set email = null where email = '' or email='null' or length(email) < 2;

SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO';
update
    oj_temp_schema.solution_temp as t
    left join (
        select
            solution_id as id,
            convert(uncompress(source_code.source) using 'utf8') AS source
        from source_code
    ) as s on
        t.orign_id = s.id
set t.source_code = s.source
where t.orign_oj=@oj_name;

update
    oj_temp_schema.solution_temp as t
    left join (
        select
            solution_id as id,
            convert(uncompress(source_code.source) using 'latin1') AS source
        from source_code
    ) as s on
        t.orign_id = s.id
   left join (
       select
            solution_id as id,
            code_length as len
       from solution
   ) as r on
       r.id = s.id
set t.source_code = s.source
where t.code_length <> char_length(t.source_code) and t.orign_oj=@oj_name;
SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '');

UPDATE
    oj_temp_schema.userprofile_temp AS t
    JOIN (
        select tb.handle, tb.email, count(rt.handle) as cnt
        from oj_temp_schema.userprofile_temp tb, oj_temp_schema.userprofile_temp rt
        where rt.handle < tb.handle and rt.email = tb.email
        group by tb.handle
        order by cnt desc
    ) as m on
        m.handle = t.handle
set
    t.email = CONCAT('$', m.cnt, '$', t.email)
where
    t.email not like '$%';

/*
delete from oj_temp_schema.userprofile_temp
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
    from oj_temp_schema.userprofile_temp;
*/
set @output_limit = 16 * 1024 * 1024;
set @next = (
    select IFNULL(max(id), 0) + 1 as next from problem
);

update
    oj.problem p
    left join (
        select *
        from oj_temp_schema.problem_temp q
        where q.new_id is not null
    ) as q on p.id = q.new_id
set
    p.creation_date = q.creation_date,
    p.hint = q.hint,
    p.input = q.input,
    p.last_update_date = q.creation_date,
    p.memory_limit = q.memory_limit,
    p.output = q.output,
    p.output_limit = 16777216,
    p.sample_input = q.sample_input,
    p.sample_output = q.sample_output,
    p.source = q.source,
    p.time_limit = q.time_limit,
    p.title = q.title
;

insert into oj.problem (
    id,
    creation_date,
    description,
    hint,
    input,
    last_update_date,
    memory_limit,
    output,
    output_limit,
    sample_input,
    sample_output,
    source,
    time_limit,
    title
) select
    new_id,
    creation_date,
    description,
    hint,
    input,
    creation_date,
    memory_limit,
    output,
    @output_limit,

from oj_temp_schema.problem_temp q
where q.new_id is not in (
    select id from oj.problem
);

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
    orign_id,
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
    oj_temp_schema.contest_temp;


INSERT INTO oj.contest(
    id,
    creation_date,
    description,
    disabled,
    last_update_date,
    name,
    title,
    type,
    creation_user,
    last_update_user
) VALUES (
    1,
    now(),
    'temp',
    0,
    now(),
    'temp',
    'temp',
    1,
    1,
    1
);

/*
select * from solution_temp
where char_length(source_code) <> code_length;
*/
/*
select * from oj_temp_schema.solution_temp
    where instr(source_code,'??')>0
    order by solution_id desc;
*/

/*
use oj_temp_schema;

create table oj_temp_schema.userprofile
    select * from oj_temp_schema.userprofile_temp union
    select * from oj_temp_schema.userprofile_java;

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
