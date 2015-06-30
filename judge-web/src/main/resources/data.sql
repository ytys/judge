-- --------------------------------------------------------
-- 主机:                           localhost
-- 服务器版本:                        10.0.17-MariaDB - mariadb.org binary distribution
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.1.0.4867
-- --------------------------------------------------------

SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT;
SET NAMES utf8mb4;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO';

-- 正在导出表  oj.contest 的数据：~3 rows (大约)
ALTER TABLE `contest` DISABLE KEYS;
REPLACE INTO `contest` (`id`, `begin_time`, `creation_date`, `disabled`, `finish_time`, `last_update_date`, `name`, `password`, `title`, `type`, `creation_user`, `last_update_user`) VALUES
	(1, NULL, now(), 0, NULL, now(), 'CLanguage', NULL, 'CLanguage', 1, 1, NULL),
	(2, NULL, now(), 0, NULL, now(), 'DataStruct', NULL, 'DataStruct', 1, 1, NULL),
	(3, NULL, now(), 0, NULL, now(), 'java', NULL, 'java', 1, 1, NULL);
ALTER TABLE `contest` ENABLE KEYS;

-- 正在导出表  oj.contest_language 的数据：~15 rows (大约)
ALTER TABLE `contest_language` DISABLE KEYS;
REPLACE INTO `contest_language` (`id`, `contest`, `language`) VALUES
	(1, 1, 1),
	(2, 1, 2),
	(3, 1, 3),
	(4, 1, 4),
	(5, 1, 5),
	(6, 2, 1),
	(7, 2, 2),
	(8, 2, 3),
	(9, 2, 4),
	(10, 2, 5),
	(11, 3, 1),
	(12, 3, 2),
	(13, 3, 3),
	(14, 3, 4),
	(15, 3, 5);
ALTER TABLE `contest_language` ENABLE KEYS;

-- 正在导出表  oj.contest_problem 的数据：~1 rows (大约)
ALTER TABLE `contest_problem` DISABLE KEYS;
REPLACE INTO `contest_problem` (`id`, `contest_order`, `title`, `contest`, `problem`) VALUES
	(1, 1000, NULL, 1, 1);
ALTER TABLE `contest_problem` ENABLE KEYS;

-- 正在导出表  oj.contest_userprofile_statistics 的数据：~1 rows (大约)
ALTER TABLE `contest_userprofile_statistics` DISABLE KEYS;
REPLACE INTO `contest_userprofile_statistics` (`id`, `contest`, `userprofile`) VALUES
	(1, 1, 1);
ALTER TABLE `contest_userprofile_statistics` ENABLE KEYS;

-- 正在导出表  oj.judge_reply 的数据：~22 rows (大约)
ALTER TABLE `judge_reply` DISABLE KEYS;
REPLACE INTO `judge_reply` (`id`, `committed`, `description`, `name`, `style`, `creation_user`, `last_update_user`) VALUES
	(0, 0, 'Queuing', 'Queuing', NULL, 1, 1),
	(1, 0, 'Compiling', 'Compiling', NULL, 1, 1),
	(2, 0, 'Running', 'Running', NULL, 1, 1),
	(3, 0, 'Runtime Error', 'Runtime Error', NULL, 1, 1),
	(4, 0, 'Wrong Answer', 'Wrong Answer', NULL, 1, 1),
	(5, 0, 'Accepted', 'Accepted', NULL, 1, 1),
	(6, 0, 'Time Limit Exceeded', 'Time Limit Exceeded', NULL, 1, 1),
	(7, 0, 'Memory Limit Exceeded', 'Memory Limit Exceeded', NULL, 1, 1),
	(8, 0, 'Out of Contest Time', 'Out of Contest Time', NULL, 1, 1),
	(9, 0, 'Restricted Function', 'Restricted Function', NULL, 1, 1),
	(10, 0, 'Output Limit Exceeded', 'Output Limit Exceeded', NULL, 1, 1),
	(11, 0, 'No such Problem', 'No such Problem', NULL, 1, 1),
	(12, 0, 'Compilation Error', 'Compilation Error', NULL, 1, 1),
	(13, 0, 'Presentation Error', 'Presentation Error', NULL, 1, 1),
	(14, 0, 'Judge Internal Error', 'Judge Internal Error', NULL, 1, 1),
	(15, 0, 'Floating Point Error', 'Floating Point Error', NULL, 1, 1),
	(16, 0, 'Segmentation Fault', 'Segmentation Fault', NULL, 1, 1),
	(17, 0, 'Prepare Compilation', 'Prepare Compilation', NULL, 1, 1),
	(18, 0, 'Prepare Execution', 'Prepare Execution', NULL, 1, 1),
	(19, 0, 'Judging', 'judging', NULL, 1, 1),
	(20, 0, 'Submission Limit Exceeded', 'Submission Limit Exceeded', NULL, 1, 1),
	(101, 0, 'Aborted', 'Aborted', NULL, 1, 1);
ALTER TABLE `judge_reply` ENABLE KEYS;

-- 正在导出表  oj.language 的数据：~5 rows (大约)
ALTER TABLE `language` DISABLE KEYS;
REPLACE INTO `language` (`id`, `compiler`, `creation_date`, `description`, `executable_extension`, `executor`, `language_extension`, `name`, `creation_user`) VALUES
	(1, '"D:\\MinGW\\bin\\g++.exe" -fno-asm -s -w -O2 -DONLINE_JUDGE -o "%PATH%%NAME%" "%PATH%%NAME%.%EXT%"', '2015-05-29 20:22:25', 'GNU C++', '', NULL, 'cc', 'G++', 1),
	(2, '"D:\\MinGW\\bin\\gcc.exe" -fno-asm -s -w -O2 -DONLINE_JUDGE -o "%PATH%%NAME%" "%PATH%%NAME%.%EXT%"', '2015-05-29 20:22:36', 'GNU C', '', NULL, 'c', 'GCC', 1),
	(3, '"C:\\JudgeOnline\\bin\\fpc\\fpc.exe" -Sg -dONLINE_JUDGE "%PATH%%NAME%.%EXT%"', '2015-05-29 20:22:45', 'Free Pascal', '', NULL, 'pas', 'Pascal', 1),
	(4, '"D:\\Program Files\\Java\\jdk1.7.0_71\\bin\\javac.exe" "%PATH%%NAME%.%EXT%"', '2015-05-29 20:22:52', 'Java 7', '', '"D:\\Program Files\\Java\\jdk1.7.0_71\\bin\\java.exe" -Djava.security.manager -Djava.security.policy=file:/C:/JudgeOnline/bin/judge.policy -classpath "%PATH%" %NAME%', 'java', 'Java', 1),
	(5, '"C:\\JudgeOnline\\bin\\vc6CompilerAdapter.bat" "D:\\Program Files\\Microsoft Visual Studio" CL.EXE /nologo /ML /W3 /GX /O2 -DONLINE_JUDGE -o %PATH%%NAME% %PATH%%NAME%.%EXT%', '2015-05-29 20:26:52', 'VC++ 6.0', '', NULL, 'cpp', 'VC++', 1);
ALTER TABLE `language` ENABLE KEYS;

-- 正在导出表  oj.limits 的数据：~1 rows (大约)
ALTER TABLE `limits` DISABLE KEYS;
REPLACE INTO `limits` (`id`, `memory_limit`, `output_limit`, `time_limit`) VALUES
	(1, 134217728, 134217728, 2000);
ALTER TABLE `limits` ENABLE KEYS;

-- 正在导出表  oj.problem 的数据：~2 rows (大约)
ALTER TABLE `problem` DISABLE KEYS;
REPLACE INTO `problem` (`id`, `creation_date`, `description`, `hint`, `input`, `last_update_date`, `output`, `sample_input`, `sample_output`, `source`, `title`, `creation_user`, `limits`) VALUES
	(1, '2015-05-29 21:45:13', 'Calculate A + B.', '', 'Each line will contain two integers A and B. Process to end of file.', '2015-06-23 11:09:40', 'For each case, output A + B in one line.', '1 1', '2', '', 'A + B Problem', 1, 1),
	(2, '2015-06-07 12:39:37', '描述', '提示', '输入', '2015-06-23 11:09:45', '输出', '样例输入', '样例输出', '源', '测试', 1, 1);
ALTER TABLE `problem` ENABLE KEYS;

-- 正在导出表  oj.role 的数据：~4 rows (大约)
ALTER TABLE `role` DISABLE KEYS;
REPLACE INTO `role` (`id`, `description`, `name`) VALUES
	(1, 'ROLE_USER', 'user'),
	(2, 'ROLE_ADMIN', 'administrator'),
	(3, 'ROLE_TEACHER', 'teacher'),
	(4, 'ROLE_STUDENT', 'student');
ALTER TABLE `role` ENABLE KEYS;

-- 正在导出表  oj.userprofile 的数据：~1 rows (大约)
ALTER TABLE `userprofile` DISABLE KEYS;
REPLACE INTO `userprofile` (`id`, `creation_date`, `disabled`, `email`, `handle`, `last_update_date`, `major`, `nickname`, `password`, `school`, `creation_user`) VALUES
        (1, now(), 0, 'system@localhost', 'system', now(), NULL, 'system', '$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG', NULL, 1),
        (2, now(), 0, 'anonymous@localhost', 'anonymousUser', now(), NULL, 'anonymousUser', '$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO', NULL, 1),
        (3, now(), 0, 'admin@localhost', 'admin', now(), NULL, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', NULL, 1);
ALTER TABLE `userprofile` ENABLE KEYS;

SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '');
SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS);
SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT;
