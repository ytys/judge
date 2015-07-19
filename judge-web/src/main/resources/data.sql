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

-- 正在导出表  oj.language 的数据：~5 rows (大约)
ALTER TABLE `language` DISABLE KEYS;
REPLACE INTO `language` (`id`, `compiler`, `creation_date`, `description`, `executable_extension`, `executor`, `language_extension`, `name`, `creation_user`) VALUES
	(1, '"D:\\MinGW\\bin\\g++.exe" -fno-asm -s -w -O2 -DONLINE_JUDGE -o "%PATH%%NAME%" "%PATH%%NAME%.%EXT%"', '2015-05-29 20:22:25', 'GNU C++', '', NULL, 'cc', 'G++', 1),
	(2, '"D:\\MinGW\\bin\\gcc.exe" -fno-asm -s -w -O2 -DONLINE_JUDGE -o "%PATH%%NAME%" "%PATH%%NAME%.%EXT%"', '2015-05-29 20:22:36', 'GNU C', '', NULL, 'c', 'GCC', 1),
	(3, '"C:\\JudgeOnline\\bin\\fpc\\fpc.exe" -Sg -dONLINE_JUDGE "%PATH%%NAME%.%EXT%"', '2015-05-29 20:22:45', 'Free Pascal', '', NULL, 'pas', 'Pascal', 1),
	(4, '"D:\\Program Files\\Java\\jdk1.7.0_71\\bin\\javac.exe" "%PATH%%NAME%.%EXT%"', '2015-05-29 20:22:52', 'Java 7', '', '"D:\\Program Files\\Java\\jdk1.7.0_71\\bin\\java.exe" -Djava.security.manager -Djava.security.policy=file:/C:/JudgeOnline/bin/judge.policy -classpath "%PATH%" %NAME%', 'java', 'Java', 1),
	(5, '"C:\\JudgeOnline\\bin\\vc6CompilerAdapter.bat" "D:\\Program Files\\Microsoft Visual Studio" CL.EXE /nologo /ML /W3 /GX /O2 -DONLINE_JUDGE -o %PATH%%NAME% %PATH%%NAME%.%EXT%', '2015-05-29 20:26:52', 'VC++ 6.0', '', NULL, 'cpp', 'VC++', 1);
ALTER TABLE `language` ENABLE KEYS;

-- 正在导出表  oj.problem 的数据：~2 rows (大约)
ALTER TABLE `problem` DISABLE KEYS;
REPLACE INTO `problem` (`id`, `creation_date`, `description`, `hint`, `input`, `last_update_date`, `output`, `sample_input`, `sample_output`, `source`, `title`, `creation_user`, `limits`) VALUES
	(1, '2015-05-29 21:45:13', 'Calculate A + B.', '', 'Each line will contain two integers A and B. Process to end of file.', '2015-06-23 11:09:40', 'For each case, output A + B in one line.', '1 1', '2', '', 'A + B Problem', 1, 1),
	(2, '2015-06-07 12:39:37', '描述', '提示', '输入', '2015-06-23 11:09:45', '输出', '样例输入', '样例输出', '源', '测试', 1, 1);
ALTER TABLE `problem` ENABLE KEYS;

SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '');
SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS);
SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT;
