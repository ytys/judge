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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
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
@Setter(AccessLevel.PACKAGE)
@Table(name = "contest", uniqueConstraints = {
    @UniqueConstraint(name = "UK_contest_name", columnNames = "name")
})
@XmlRootElement
public class Contest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @JsonIgnore
    private Long id;

    @Column(length = 255)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9-_\\.]*+$", message = "{Error}")
    @Length(min = 1, max = 255)
    private String name;

    @Column(length = Integer.MAX_VALUE)
    @Lob
    @Length(min = 1, max = Integer.MAX_VALUE)
    private String title;

    @Column(length = 255)
    @JsonIgnore
    @Length(min = 1, max = 255)
    private String password;

    @Column(name = "begin_time")
    private LocalDateTime beginTime;

    @Column(name = "finish_time")
    private LocalDateTime finishTime;

    @JoinColumn(name = "parent", foreignKey = @ForeignKey(name = "FK_contest_parent"))
    @JsonIgnore
    @ManyToOne
    private Contest parent;

    @Column(name = "description")
    private String description;

    @Column(name = "disabled", nullable = false)
    @JsonIgnore
    private boolean disabled;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private ContestType type;

    @Column(name = "creation_date")
    @CreatedDate
    @JsonIgnore
    private LocalDateTime creationDate;

    @CreatedBy
    @JoinColumn(name = "creation_user", foreignKey = @ForeignKey(name = "FK_contest_creation_user"))
    @JsonIgnore
    @ManyToOne
    private Userprofile createdBy;

    @Column(name = "last_update_date")
    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime lastModifiedDate;

    @LastModifiedBy
    @JoinColumn(name = "last_update_user", foreignKey = @ForeignKey(name = "FK_contest_last_update_user"))
    @JsonIgnore
    @ManyToOne
    private Userprofile lastModifiedBy;

}
