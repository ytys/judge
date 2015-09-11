/*
 * Copyright 2015 zhanhb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.zhanhb.judge.core;

import com.github.zhanhb.judge.domain.JudgeReply;
import com.github.zhanhb.judge.domain.Submission;
import com.github.zhanhb.judge.service.JudgeService;
import java.util.LinkedHashSet;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
@Slf4j
public class Judger {

    private final ThreadGroup judgeGroup = new ThreadGroup("judge group");
    private final LinkedHashSet<Long> queue = new LinkedHashSet<>(200);
    private final Object judgeLock = new Object();
    @Autowired
    private JudgeService judgeService;
    private boolean shutdown = false;

    @PostConstruct
    public void start() {
        synchronized (judgeGroup) {
            if (shutdown) {
                throw new IllegalStateException("judge system is shutting down");
            }
            if (judgeGroup.activeGroupCount() < 1) {
                new Thread(judgeGroup, Judger.this::runInternal, "judger").start();
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        synchronized (judgeGroup) {
            shutdown = true;
            judgeGroup.interrupt();
        }
    }

    protected void runInternal() {
        while (true) {
            long id;
            synchronized (queue) {
                if (queue.isEmpty()) {
                    try {
                        queue.wait();
                        // notify call by other components.
                        if (queue.isEmpty()) {
                            continue;
                        }
                    } catch (InterruptedException ex) {
                        return;
                    }
                }
                // get the first submission id from the queue
                id = queue.iterator().next();
            }
            try {
                judgeService.findOne(id).ifPresent(submission -> {
                    try {
                        judgeService.updataStatus(submission, JudgeReply.Compiling);
                        synchronized (judgeLock) {
                            if (compile(submission)) {
                                judgeService.updataStatus(submission, JudgeReply.Running);
                                runProcess(submission);
                            }
                        }
                    } catch (Throwable ex) {
                        log.error("", ex);
                        judgeService.updataStatus(submission, JudgeReply.JudgeInternalError);
                    }
                });
            } finally {
                queue.remove(id);
            }
        }
    }

    private boolean compile(Submission submission) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void runProcess(Submission submission) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
