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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
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
import javax.persistence.Lob;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
@Table(name = "problem")
@XmlRootElement
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(length = Integer.MAX_VALUE)
    @Lob
    @NotNull
    @Length(min = 1, max = Integer.MAX_VALUE)
    private String title;

    @Lob
    @Column(length = Integer.MAX_VALUE)
    @NotNull
    @Length(min = 0, max = Integer.MAX_VALUE)
    private String description;

    @Column(length = Integer.MAX_VALUE)
    @Lob
    @NotNull
    @Length(min = 0, max = Integer.MAX_VALUE)
    private String input;

    @Column(length = Integer.MAX_VALUE)
    @Lob
    @NotNull
    @Length(min = 0, max = Integer.MAX_VALUE)
    private String output;

    @Column(name = "sample_input", length = Integer.MAX_VALUE)
    @Lob
    @NotNull
    @Length(min = 0, max = Integer.MAX_VALUE)
    private String sampleInput;

    @Column(name = "sample_output", length = Integer.MAX_VALUE)
    @Lob
    @NotNull
    @Length(min = 0, max = Integer.MAX_VALUE)
    private String sampleOutput;

    @Column(length = Integer.MAX_VALUE)
    @Lob
    @NotNull
    @Length(min = 0, max = Integer.MAX_VALUE)
    private String hint;

    @Column(length = Integer.MAX_VALUE)
    @Lob
    @NotNull
    @Length(min = 0, max = Integer.MAX_VALUE)
    private String source;

    @JoinColumn(name = "limits", foreignKey = @ForeignKey(name = "FK_problem_limits"))
    @ManyToOne
    @JsonUnwrapped
    private Limits limits;

    @Column(name = "creation_date")
    @CreatedDate
    @JsonIgnore
    private LocalDateTime creationDate;

    @CreatedBy
    @JoinColumn(name = "creation_user", foreignKey = @ForeignKey(name = "FK_problem_creation_user"))
    @JsonIgnore
    @ManyToOne
    private Userprofile creationUser;

    @Column(name = "last_update_date")
    @JsonIgnore
    @LastModifiedDate
    private LocalDateTime lastUpdateDate;

}
