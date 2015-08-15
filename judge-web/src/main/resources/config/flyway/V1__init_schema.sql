-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.0.20-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             9.1.0.4867
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping structure for table oj_test_db.access_log
CREATE TABLE IF NOT EXISTS access_log (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  access_time datetime DEFAULT NULL,
  behaviour varchar(255) DEFAULT NULL,
  handle varchar(255) DEFAULT NULL,
  ip varchar(255) DEFAULT NULL,
  url varchar(255) DEFAULT NULL,
  userprofile bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_access_log_userprofile (userprofile),
  CONSTRAINT FK_access_log_userprofile FOREIGN KEY (userprofile) REFERENCES userprofile (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.contest
CREATE TABLE IF NOT EXISTS contest (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  begin_time datetime DEFAULT NULL,
  creation_date datetime DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  disabled bit(1) NOT NULL,
  finish_time datetime DEFAULT NULL,
  last_update_date datetime DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  title longtext,
  type int(11) NOT NULL,
  creation_user bigint(20) DEFAULT NULL,
  last_update_user bigint(20) DEFAULT NULL,
  parent bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_contest_name (name),
  KEY FK_contest_creation_user (creation_user),
  KEY FK_contest_last_update_user (last_update_user),
  KEY FK_contest_parent (parent),
  CONSTRAINT FK_contest_creation_user FOREIGN KEY (creation_user) REFERENCES userprofile (id),
  CONSTRAINT FK_contest_last_update_user FOREIGN KEY (last_update_user) REFERENCES userprofile (id),
  CONSTRAINT FK_contest_parent FOREIGN KEY (parent) REFERENCES contest (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.contest_language
CREATE TABLE IF NOT EXISTS contest_language (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  contest bigint(20) NOT NULL,
  language bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_contest_language_contest_language (contest,language),
  KEY FK_contest_language_language (language),
  CONSTRAINT FK_contest_language_contest FOREIGN KEY (contest) REFERENCES contest (id),
  CONSTRAINT FK_contest_language_language FOREIGN KEY (language) REFERENCES language (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.contest_problem
CREATE TABLE IF NOT EXISTS contest_problem (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  contest_order bigint(20) NOT NULL,
  title longtext,
  contest bigint(20) NOT NULL,
  problem bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_contest_problem_contest_problem (contest,problem),
  UNIQUE KEY UK_contest_problem_contest_contest_order (contest,contest_order),
  KEY FK_contest_problem_problem (problem),
  CONSTRAINT FK_contest_problem_contest FOREIGN KEY (contest) REFERENCES contest (id),
  CONSTRAINT FK_contest_problem_problem FOREIGN KEY (problem) REFERENCES problem (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.contest_problem_statistics
CREATE TABLE IF NOT EXISTS contest_problem_statistics (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  accepted bigint(20) NOT NULL,
  solved bigint(20) NOT NULL,
  submit bigint(20) NOT NULL,
  submit_user bigint(20) NOT NULL,
  contest_problem bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_contest_problem_statistics_contest_problem (contest_problem),
  CONSTRAINT FK_contest_problem_statistics_contest_problem FOREIGN KEY (contest_problem) REFERENCES contest_problem (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.contest_userprofile_statistics
CREATE TABLE IF NOT EXISTS contest_userprofile_statistics (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  contest bigint(20) NOT NULL,
  userprofile bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_contest_userprofile_statistics_contest_userprofile (contest,userprofile),
  KEY FK_contest_userprofile_statistics_userprofile (userprofile),
  CONSTRAINT FK_contest_userprofile_statistics_contest FOREIGN KEY (contest) REFERENCES contest (id),
  CONSTRAINT FK_contest_userprofile_statistics_userprofile FOREIGN KEY (userprofile) REFERENCES userprofile (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.judge_reply
CREATE TABLE IF NOT EXISTS judge_reply (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  committed smallint(6) NOT NULL,
  description varchar(128) DEFAULT NULL,
  name varchar(32) NOT NULL,
  style varchar(32) DEFAULT NULL,
  creation_user bigint(20) DEFAULT NULL,
  last_update_user bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_judge_reply_creation_user (creation_user),
  KEY FK_judge_reply_last_update_user (last_update_user),
  CONSTRAINT FK_judge_reply_creation_user FOREIGN KEY (creation_user) REFERENCES userprofile (id),
  CONSTRAINT FK_judge_reply_last_update_user FOREIGN KEY (last_update_user) REFERENCES userprofile (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.language
CREATE TABLE IF NOT EXISTS language (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  compiler varchar(255) DEFAULT NULL,
  creation_date datetime DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  executable_extension varchar(32) NOT NULL,
  executor varchar(255) DEFAULT NULL,
  language_extension varchar(32) NOT NULL,
  name varchar(50) DEFAULT NULL,
  creation_user bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_language_creation_user (creation_user),
  CONSTRAINT FK_language_creation_user FOREIGN KEY (creation_user) REFERENCES userprofile (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.persistent_token
CREATE TABLE IF NOT EXISTS persistent_token (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  userprofile bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_persistent_token_userprofile (userprofile),
  CONSTRAINT FK_persistent_token_userprofile FOREIGN KEY (userprofile) REFERENCES userprofile (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.problem
CREATE TABLE IF NOT EXISTS problem (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  case_time_limit bigint(20) DEFAULT NULL,
  creation_date datetime DEFAULT NULL,
  description longtext,
  hint longtext,
  input longtext,
  last_update_date datetime DEFAULT NULL,
  memory_limit bigint(20) NOT NULL,
  output longtext,
  output_limit bigint(20) NOT NULL,
  sample_input longtext,
  sample_output longtext,
  source longtext,
  time_limit bigint(20) NOT NULL,
  title longtext NOT NULL,
  creation_user bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_problem_creation_user (creation_user),
  CONSTRAINT FK_problem_creation_user FOREIGN KEY (creation_user) REFERENCES userprofile (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.role
CREATE TABLE IF NOT EXISTS role (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  description longtext,
  name varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_role_name (name)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.submission
CREATE TABLE IF NOT EXISTS submission (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  code_len bigint(20) NOT NULL,
  ip varchar(40) DEFAULT NULL,
  judge_time datetime DEFAULT NULL,
  memory bigint(20) DEFAULT NULL,
  submit_time datetime NOT NULL,
  time bigint(20) DEFAULT NULL,
  contest bigint(20) DEFAULT NULL,
  judge_reply bigint(20) DEFAULT NULL,
  language bigint(20) NOT NULL,
  problem bigint(20) NOT NULL,
  userprofile bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_submission_contest (contest),
  KEY FK_submission_judge_reply (judge_reply),
  KEY FK_submission_language (language),
  KEY FK_submission_problem (problem),
  KEY FK_submission_userprofile (userprofile),
  CONSTRAINT FK_submission_contest FOREIGN KEY (contest) REFERENCES contest (id),
  CONSTRAINT FK_submission_judge_reply FOREIGN KEY (judge_reply) REFERENCES judge_reply (id),
  CONSTRAINT FK_submission_language FOREIGN KEY (language) REFERENCES language (id),
  CONSTRAINT FK_submission_problem FOREIGN KEY (problem) REFERENCES problem (id),
  CONSTRAINT FK_submission_userprofile FOREIGN KEY (userprofile) REFERENCES userprofile (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.submission_compile_info
CREATE TABLE IF NOT EXISTS submission_compile_info (
  compile_info longtext,
  id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_submission_compile_info_submission FOREIGN KEY (id) REFERENCES submission (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.submission_judge_detail
CREATE TABLE IF NOT EXISTS submission_judge_detail (
  detail longtext,
  id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_submission_judge_detail_submission FOREIGN KEY (id) REFERENCES submission (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.submission_source
CREATE TABLE IF NOT EXISTS submission_source (
  source_code longtext,
  id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_submission_source_submission FOREIGN KEY (id) REFERENCES submission (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.upload
CREATE TABLE IF NOT EXISTS upload (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  content longblob,
  content_type varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  orign_name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.usergroup
CREATE TABLE IF NOT EXISTS usergroup (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  description longtext NOT NULL,
  group_order bigint(20) NOT NULL,
  name varchar(255) NOT NULL,
  owner bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_usergroup_owner_group_order (owner,group_order),
  CONSTRAINT FK_usergroup_owner FOREIGN KEY (owner) REFERENCES userprofile (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.usergroup_userprofile
CREATE TABLE IF NOT EXISTS usergroup_userprofile (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  usergroup bigint(20) NOT NULL,
  userprofile bigint(20) NOT NULL,
  PRIMARY KEY (id),
  KEY FK_usergroup_userprofile_usergroup (usergroup),
  KEY FK_usergroup_userprofile_userprofile (userprofile),
  CONSTRAINT FK_usergroup_userprofile_usergroup FOREIGN KEY (usergroup) REFERENCES usergroup (id),
  CONSTRAINT FK_usergroup_userprofile_userprofile FOREIGN KEY (userprofile) REFERENCES userprofile (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.userprofile
CREATE TABLE IF NOT EXISTS userprofile (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  birth_date date DEFAULT NULL,
  creation_date datetime DEFAULT NULL,
  disabled bit(1) NOT NULL,
  email varchar(255) DEFAULT NULL,
  handle varchar(255) DEFAULT NULL,
  last_update_date datetime DEFAULT NULL,
  major varchar(255) DEFAULT NULL,
  nickname varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  school varchar(255) DEFAULT NULL,
  creation_user bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_userprofile_handle (handle),
  UNIQUE KEY UK_userprofile_email (email),
  KEY FK_userprofile_creation_user (creation_user),
  CONSTRAINT FK_userprofile_creation_user FOREIGN KEY (creation_user) REFERENCES userprofile (id)
);

-- Data exporting was unselected.


-- Dumping structure for table oj_test_db.userprofile_role
CREATE TABLE IF NOT EXISTS userprofile_role (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  role bigint(20) NOT NULL,
  userprofile bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UQ_userprofile_role (userprofile,role),
  KEY FK_userprofile_role_role (role),
  CONSTRAINT FK_userprofile_role_role FOREIGN KEY (role) REFERENCES role (id),
  CONSTRAINT FK_userprofile_role_userprofile FOREIGN KEY (userprofile) REFERENCES userprofile (id)
);

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
