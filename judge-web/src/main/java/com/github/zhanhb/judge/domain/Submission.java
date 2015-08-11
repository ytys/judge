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
package com.github.zhanhb.judge.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * @author zhanhb
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PACKAGE)
@Table(name = "submission")
@XmlRootElement
public class Submission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "code_len", nullable = false)
    @NotNull
    private long codeLen;

    @Column(name = "submit_time")
    @CreatedDate
    @NotNull
    private LocalDateTime submitTime;

    @Column(name = "judge_time")
    private LocalDateTime judgeTime;

    @JoinColumn(name = "contest", foreignKey = @ForeignKey(name = "FK_submission_contest"))
    @ManyToOne
    private Contest contest;

    @JoinColumn(name = "judge_reply", foreignKey = @ForeignKey(name = "FK_submission_judge_reply"))
    @ManyToOne
    private JudgeReply judgeReply;

    @JoinColumn(name = "language", foreignKey = @ForeignKey(name = "FK_submission_language"))
    @ManyToOne(optional = false)
    private Language language;

    @JoinColumn(name = "problem", foreignKey = @ForeignKey(name = "FK_submission_problem"))
    @ManyToOne(optional = false)
    private Problem problem;

    @CreatedBy
    @JoinColumn(name = "userprofile", foreignKey = @ForeignKey(name = "FK_submission_userprofile"))
    @ManyToOne
    private Userprofile userprofile;

    @Column(name = "time")
    private Long time;

    @Column(name = "memory")
    private Long memory;

    @Column(name = "ip", length = 40)
    private String ip;

}
