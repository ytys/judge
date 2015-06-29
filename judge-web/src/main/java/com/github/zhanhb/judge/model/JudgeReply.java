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
package com.github.zhanhb.judge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
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
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * @author zhanhb
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "judge_reply")
@XmlRootElement
public class JudgeReply implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 32)
    @NotNull
    @Length(min = 1, max = 32)
    private String name;

    @Column(length = 128)
    @Length(max = 128)
    private String description;

    @Column(length = 32)
    @Length(max = 32)
    private String style;

    @NotNull
    @Column(nullable = false)
    private short committed;

    @CreatedBy
    @JoinColumn(name = "creation_user", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_judge_reply_creation_user"))
    @JsonIgnore
    @ManyToOne
    private Userprofile creationUser;

    @JoinColumn(name = "last_update_user", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_judge_reply_last_update_user"))
    @ManyToOne
    private Userprofile lastUpdateUser;

}
