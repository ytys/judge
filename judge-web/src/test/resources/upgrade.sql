use clanguage;

create database if not exists oj_temp_schema COLLATE 'utf8_general_ci';

CREATE TABLE IF NOT EXISTS oj_temp_schema.contest_temp (
    orign_oj VARCHAR(255) NULL DEFAULT NULL,
    orign_id BIGINT(20) NULL DEFAULT NULL,
    title LONGTEXT NULL,
    start_time DATETIME NULL DEFAULT NULL,
    end_time DATETIME NULL DEFAULT NULL,
    description LONGTEXT NULL,
    disabled BIT(1) NOT NULL DEFAULT b'0',
    new_id BIGINT(20) NULL DEFAULT NULL,
    type INT(11) NULL DEFAULT NULL,
    UNIQUE INDEX orign_oj_orign_id (orign_oj, orign_id),
    UNIQUE INDEX new_id (new_id)
);

CREATE TABLE IF NOT EXISTS oj_temp_schema.problem_temp (
    orign_oj VARCHAR(255) NOT NULL,
    orign_id BIGINT(20) NOT NULL,
    title VARCHAR(255) NULL DEFAULT NULL,
    description LONGTEXT NULL,
    input LONGTEXT NULL,
    output LONGTEXT NULL,
    sample_input LONGTEXT NULL,
    sample_output LONGTEXT NULL,
    hint LONGTEXT NULL,
    source VARCHAR(100) NULL DEFAULT NULL,
    sample_Program VARCHAR(255) NULL DEFAULT NULL,
    creation_date DATETIME NULL DEFAULT NULL,
    time_limit INT(11) NULL DEFAULT NULL,
    memory_limit INT(11) NULL DEFAULT NULL,
    case_time_limit INT(11) NULL DEFAULT NULL,
    disabled BIT(1) NULL DEFAULT b'0',
    new_id BIGINT(20) NULL DEFAULT NULL,
    PRIMARY KEY (orign_oj, orign_id),
    UNIQUE INDEX new_id (new_id)
);

CREATE TABLE IF NOT EXISTS oj_temp_schema.solution_temp (
    orign_oj VARCHAR(255) NOT NULL,
    orign_id BIGINT(20) NOT NULL,
    orign_problem_id BIGINT(11) NOT NULL,
    orign_user_handle VARCHAR(255) NOT NULL DEFAULT '',
    orign_contest_id BIGINT(20) NULL DEFAULT NULL,
    time INT(11) NOT NULL DEFAULT '0',
    memory INT(11) NOT NULL DEFAULT '0',
    in_date DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00',
    result SMALLINT(6) NOT NULL DEFAULT '0',
    language TINYINT(4) NOT NULL,
    ip VARCHAR(32) NOT NULL DEFAULT '',
    code_length INT(11) NOT NULL DEFAULT '0',
    source_code LONGTEXT NULL,
    compile_info LONGTEXT NULL,
    new_id BIGINT(20) NULL DEFAULT NULL,
    PRIMARY KEY (orign_oj, orign_id),
    UNIQUE INDEX new_id (new_id)
);

CREATE TABLE IF NOT EXISTS oj_temp_schema.userprofile_temp (
    orign_oj VARCHAR(255) NOT NULL,
    handle VARCHAR(255) NOT NULL,
    birth_date DATETIME NULL DEFAULT NULL,
    creation_date DATETIME NULL DEFAULT NULL,
    disabled BIT(1) NOT NULL DEFAULT b'0',
    email LONGTEXT NULL,
    last_update_date DATETIME NULL DEFAULT NULL,
    major LONGTEXT NULL,
    nickname LONGTEXT NULL,
    password LONGTEXT NULL,
    school LONGTEXT NULL,
    new_id BIGINT(20) NULL DEFAULT NULL,
    PRIMARY KEY (orign_oj, handle),
    INDEX KEY_email (email(100)) USING HASH
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
    IF(defunct='Y', 1, 0),
    replace(trim(email), 194, ''),
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

/* OJ */
insert ignore into oj.contest (
    creation_date,
    description,
    disabled,
    last_update_date,
    name,
    title,
    type
) select
    now(),
    @oj_name,
    0,
    now(),
    @oj_name,
    @oj_name,
    1 /* OJ */
;

set @oj_id = (
    select id from oj.contest where name = @oj_name
);

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
where t.source_code is null and t.orign_oj=@oj_name;

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

set @output_limit = 16 * 1024 * 1024;
ALTER TABLE oj.problem AUTO_INCREMENT=0;

ALTER TABLE oj.problem
    DROP COLUMN IF EXISTS orign_oj,
    DROP COLUMN IF EXISTS orign_id;
ALTER TABLE oj.problem
    ADD COLUMN orign_oj VARCHAR(255) NULL DEFAULT NULL FIRST,
    ADD COLUMN orign_id BIGINT(20) NULL DEFAULT NULL AFTER orign_oj;
UPDATE oj_temp_schema.problem_temp q SET q.new_id = null WHERE q.new_id not in (
    select id from oj.problem
);
insert into oj.problem (
    orign_oj,
    orign_id,
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
    case_time_limit,
    title
) select
    orign_oj,
    orign_id,
    creation_date,
    description,
    hint,
    input,
    creation_date,
    memory_limit,
    output,
    @output_limit,
    sample_input,
    sample_output,
    source,
    time_limit,
    case_time_limit,
    title
from
    oj_temp_schema.problem_temp q
where q.new_id IS NULL;

update
    oj_temp_schema.problem_temp p
left join
    oj.problem q
on
    p.orign_oj = q.orign_oj and p.orign_id = q.orign_id
set
    p.new_id = q.id
where p.new_id is null;
ALTER TABLE oj.problem
    DROP COLUMN orign_oj,
    DROP COLUMN orign_id;

/* contests */
ALTER TABLE oj.contest
    DROP COLUMN IF EXISTS orign_oj,
    DROP COLUMN IF EXISTS orign_id;

ALTER TABLE oj.contest
    ADD COLUMN orign_oj VARCHAR(255) NULL DEFAULT NULL FIRST,
    ADD COLUMN orign_id BIGINT(20) NULL DEFAULT NULL AFTER orign_oj;

update oj_temp_schema.contest_temp q
set q.new_id = null
where q.new_id not in (
    select id from oj.contest
);

ALTER TABLE oj.contest AUTO_INCREMENT=0;

insert into oj.contest (
    orign_oj,
    orign_id,
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
    orign_oj,
    orign_id,
    start_time,
    now(),
    disabled,
    end_time,
    now(),
    concat(orign_oj, '_', orign_id),
    NULL,
    title,
    2, /* CONTEST */
    1, /* system user */
    1, /* system user */
    description,
    @oj_id
from
    oj_temp_schema.contest_temp q
where q.new_id IS NULL;

update oj_temp_schema.contest_temp p
left join oj.contest q
on p.orign_oj = q.orign_oj and p.orign_id = q.orign_id
set new_id = q.id
where new_id is null;

ALTER TABLE oj.contest
    DROP COLUMN IF EXISTS orign_oj,
    DROP COLUMN IF EXISTS orign_id;


update oj_temp_schema.userprofile_temp
set new_id = null
where new_id not in (
    select id from oj.userprofile
);

insert ignore into oj.userprofile (
    birth_date,
    creation_date,
    disabled,
    email,
    handle,
    last_update_date,
    nickname,
    password,
    school
) select
    birth_date,
    ifnull(creation_date, ifnull(last_update_date,now())),
    disabled,
    email,
    handle,
    last_update_date,
    nickname,
    password,
    school
    from oj_temp_schema.userprofile_temp;

update oj_temp_schema.userprofile_temp p
left join oj.userprofile q
on p.handle = q.handle
set p.new_id = q.id
where p.new_id is null;

update oj_temp_schema.userprofile_temp p
left join oj.userprofile q
on p.email = q.email
set p.new_id = q.id
where p.new_id is null;

update oj.userprofile p
right join(
    select * from oj_temp_schema.userprofile_temp
    where disabled = 0
) q on q.new_id = p.id
set p.disabled = 0;

update oj_temp_schema.solution_temp
set new_id = null
where new_id not in (
    select id from oj.submission
);

ALTER TABLE oj.submission
    DROP COLUMN IF EXISTS orign_oj,
    DROP COLUMN IF EXISTS orign_id;

ALTER TABLE oj.submission
    ADD COLUMN orign_oj VARCHAR(255) NULL DEFAULT NULL FIRST,
    ADD COLUMN orign_id BIGINT(20) NULL DEFAULT NULL AFTER orign_oj;

ALTER TABLE oj.submission AUTO_INCREMENT=0;

insert into oj.submission (
    orign_oj,
    orign_id,
    problem,
    userprofile,
    contest,
    time,
    memory,
    submit_time,
    language,
    ip,
    code_len,
    compile_info,
    source_code
) select
    orign_oj,
    orign_id,
    problem,
    userprofile,
    ifnull(contest, @oj_id),
    time,
    memory,
    in_date,
    language + 1,
    ip,
    code_length,
    compile_info,
    source_code
from (
    select * from oj_temp_schema.solution_temp s
    left join (
        select new_id as contest, orign_id cid, orign_oj coj
        from oj_temp_schema.contest_temp
    ) c on s.orign_contest_id = c.cid and s.orign_oj = c.coj
    left join (
        select new_id as userprofile, handle uh, orign_oj uoj
        from oj_temp_schema.userprofile_temp
    ) u on s.orign_user_handle = u.uh and s.orign_oj = u.uoj
    left join (
        select new_id as problem, orign_id pid, orign_oj poj
        from oj_temp_schema.problem_temp
    ) p on s.orign_problem_id = p.pid and s.orign_oj = p.poj
) s
where
    s.new_id is null
;

update oj_temp_schema.solution_temp p
left join (
    select id, orign_oj, orign_id
    from oj.submission
) q on p.orign_oj = q.orign_oj and p.orign_id = q.orign_id
set p.new_id = q.id
where p.new_id is null;

ALTER TABLE oj.submission
    DROP COLUMN IF EXISTS orign_oj,
    DROP COLUMN IF EXISTS orign_id;

update oj.submission p
left join (
    select
        new_id,
        result
    from oj_temp_schema.solution_temp
) q on p.id = q.new_id
    set p.judge_reply = 6
where
    q.result = 100 and
    p.judge_reply is null
;

/* contest problem start */
ALTER TABLE contest_problem AUTO_INCREMENT=0;
replace into oj.contest_problem (
    id,
    contest_order,
    contest,
    problem
) select
    t.id,
    orign_id,
    @oj_id,
    new_id
from
    oj_temp_schema.problem_temp p
left join (
    select
        id,
        contest,
        problem
    from oj.contest_problem
) t
    on t.problem = p.new_id and t.contest = @oj_id
;
/* contest problem end */

/*
select
    orign_oj,
    orign_id,
    problem,
    userprofile,
    contest,
    time,
    memory,
    in_date,
    result,
    language,
    ip,
    code_length,
    compile_info,
    new_id
from (
    select * from oj_temp_schema.solution_temp s
    left join (
        select new_id as contest, orign_id cid, orign_oj coj
        from oj_temp_schema.contest_temp
    ) c on s.orign_contest_id = c.cid and s.orign_oj = c.coj
    left join (
        select new_id as userprofile, handle uh, orign_oj uoj
        from oj_temp_schema.userprofile_temp
    ) u on s.orign_user_handle = u.uh and s.orign_oj = u.uoj
    left join (
        select new_id as problem, orign_id pid, orign_oj poj
        from oj_temp_schema.problem_temp
    ) p on s.orign_problem_id = p.pid and s.orign_oj = p.poj
) s
;
*/

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

INSERT IGNORE INTO oj.language (id, compiler, creation_date, description, executable_extension, executor, language_extension, name, creation_user) VALUES
    (1, '"D:\\MinGW\\bin\\g++.exe" -fno-asm -s -w -O2 -DONLINE_JUDGE -o "%PATH%%NAME%" "%PATH%%NAME%.%EXT%"', '2015-08-06 13:17:44', 'GNU C++', 'exe', NULL, 'cpp', 'GNU C++', 1),
    (2, '"D:\\MinGW\\bin\\gcc.exe" -fno-asm -s -w -O2 -DONLINE_JUDGE -o "%PATH%%NAME%" "%PATH%%NAME%.%EXT%"', '2015-08-06 13:18:59', 'GNU C', 'exe', NULL, 'c', 'GNU C', 1),
    (3, '"C:\\JudgeOnline\\bin\\fpc\\fpc.exe" -Sg -dONLINE_JUDGE "%PATH%%NAME%.%EXT%"', '2015-08-06 13:19:35', 'Pascal', 'exe', NULL, 'pas', 'Pascal', 1),
    (4, '"D:\\Program Files\\Java\\jdk1.7.0_79\\bin\\javac.exe" "%PATH%%NAME%.%EXT%"', '2015-08-06 13:20:20', 'Java 7', 'class', '"D:\\Program Files\\Java\\jdk1.7.0_79\\bin\\java.exe" -Djava.security.manager -Djava.security.policy=file:/C:/JudgeOnline/bin/judge.policy -classpath "%PATH%" %NAME%', 'java', 'Java', 1),
    (5, '"C:\\JudgeOnline\\bin\\vc6CompilerAdapter.bat" "D:\\Program Files\\Microsoft Visual Studio" CL.EXE /nologo /ML /W3 /GX /O2 -DONLINE_JUDGE -o %PATH%%NAME% %PATH%%NAME%.%EXT%', '2015-08-06 13:21:10', 'VC++ 6.0', 'exe', NULL, 'cpp', 'VC++', 1);


*/
