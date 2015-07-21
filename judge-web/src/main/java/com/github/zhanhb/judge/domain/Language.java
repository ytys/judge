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
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
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
@Table(name = "language")
@XmlRootElement
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Length(max = 50)
    @Column(length = 50)
    private String name;

    @Length(min = 1, max = 255)
    @Column
    private String description;

    @Length(min = 1, max = 255)
    @Column
    private String compiler;

    @Length(min = 1, max = 255)
    @Column
    private String executor;

    @Column(name = "language_extension", nullable = false, length = 32)
    @NotNull
    @Length(min = 1, max = 32)
    private String languageExtension;

    @Column(name = "executable_extension", nullable = false, length = 32)
    @NotNull
    @Length(min = 1, max = 32)
    private String executableExtension;

    @Column(name = "creation_date", nullable = false)
    @CreatedDate
    @JsonIgnore
    private LocalDateTime creationDate;

    @CreatedBy
    @JoinColumn(name = "creation_user", foreignKey = @ForeignKey(name = "FK_language_creation_user"))
    @JsonIgnore
    @ManyToOne
    private Userprofile creationUser;

}
