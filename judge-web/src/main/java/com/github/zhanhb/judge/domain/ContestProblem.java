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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
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
@Table(name = "contest_problem", uniqueConstraints = {
    @UniqueConstraint(name = "UK_contest_problem_contest_problem", columnNames = {"contest", "problem"}),
    @UniqueConstraint(name = "UK_contest_problem_contest_contest_order", columnNames = {"contest", "contest_order"})
})
@XmlRootElement
public class ContestProblem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "contest_order", nullable = false)
    private long contestOrder;

    @Column(length = Integer.MAX_VALUE)
    @Lob
    @Length(min = 0, max = Integer.MAX_VALUE)
    private String title;

    @JoinColumn(name = "contest", nullable = false, foreignKey = @ForeignKey(name = "FK_contest_problem_contest"))
    @ManyToOne(optional = false)
    private Contest contest;

    @JoinColumn(name = "problem", nullable = false, foreignKey = @ForeignKey(name = "FK_contest_problem_problem"))
    @ManyToOne(optional = false)
    private Problem problem;

}
