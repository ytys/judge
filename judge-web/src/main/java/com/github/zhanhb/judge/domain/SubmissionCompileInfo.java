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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * @author zhanhb
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = "submission")
@Table(name = "submission_compile_info")
public class SubmissionCompileInfo implements Serializable {

    @Id
    @JoinColumn(name = "id", foreignKey = @ForeignKey(name = "FK_submission_compile_info_submission"), insertable = false, updatable = false, nullable = false)
    @OneToOne
    private Submission submission;

    @Column(name = "compile_info", length = Integer.MAX_VALUE)
    @Lob
    private String compileInfo;

}
