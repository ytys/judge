INSERT IGNORE INTO userprofile (id, creation_date, disabled, email, handle, last_update_date, major, nickname, password, school, creation_user) VALUES
    (1, now(), 0, 'system@localhost', 'system', now(), NULL, 'system', '$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG', NULL, NULL),
    (2, now(), 0, 'anonymous@localhost', 'anonymousUser', now(), NULL, 'anonymousUser', '$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO', NULL, NULL),
    (3, now(), 0, 'admin@localhost', 'admin', now(), NULL, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', NULL, NULL);

INSERT IGNORE INTO judge_reply (id, committed, description, name, style, creation_user, last_update_user) VALUES
    (1, 0, 'Queuing', 'Queuing', NULL, 1, 1),
    (2, 0, 'Compiling', 'Compiling', NULL, 1, 1),
    (3, 0, 'Running', 'Running', NULL, 1, 1),
    (4, 0, 'Runtime Error', 'Runtime Error', NULL, 1, 1),
    (5, 0, 'Wrong Answer', 'Wrong Answer', NULL, 1, 1),
    (6, 0, 'Accepted', 'Accepted', NULL, 1, 1),
    (7, 0, 'Time Limit Exceeded', 'Time Limit Exceeded', NULL, 1, 1),
    (8, 0, 'Memory Limit Exceeded', 'Memory Limit Exceeded', NULL, 1, 1),
    (9, 0, 'Out of Contest Time', 'Out of Contest Time', NULL, 1, 1),
    (10, 0, 'Restricted Function', 'Restricted Function', NULL, 1, 1),
    (11, 0, 'Output Limit Exceeded', 'Output Limit Exceeded', NULL, 1, 1),
    (12, 0, 'No such Problem', 'No such Problem', NULL, 1, 1),
    (13, 0, 'Compilation Error', 'Compilation Error', NULL, 1, 1),
    (14, 0, 'Presentation Error', 'Presentation Error', NULL, 1, 1),
    (15, 0, 'Judge Internal Error', 'Judge Internal Error', NULL, 1, 1),
    (16, 0, 'Floating Point Error', 'Floating Point Error', NULL, 1, 1),
    (17, 0, 'Segmentation Fault', 'Segmentation Fault', NULL, 1, 1),
    (18, 0, 'Prepare Compilation', 'Prepare Compilation', NULL, 1, 1),
    (19, 0, 'Prepare Execution', 'Prepare Execution', NULL, 1, 1),
    (20, 0, 'Judging', 'judging', NULL, 1, 1),
    (21, 0, 'Submission Limit Exceeded', 'Submission Limit Exceeded', NULL, 1, 1),
    (101, 0, 'Aborted', 'Aborted', NULL, 1, 1);

INSERT IGNORE INTO role (id, description, name) VALUES
    (1, 'administrator', 'ROLE_ADMIN'),
    (2, 'user', 'ROLE_USER'),
    (3, 'teacher', 'ROLE_TEACHER');

INSERT IGNORE INTO userprofile_role (id, role, userprofile) VALUES
    (1, 1, 3),
    (2, 2, 3);
