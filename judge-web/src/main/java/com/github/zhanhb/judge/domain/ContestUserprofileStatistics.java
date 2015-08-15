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
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

/**
 *
 * @author zhanhb
 */
@Data
@Entity
@EqualsAndHashCode(of = {"contest", "userprofile"})
@Setter(AccessLevel.PACKAGE)
@Table(name = "contest_userprofile_statistics", uniqueConstraints = {
    @UniqueConstraint(name = "UK_contest_userprofile_statistics_contest_userprofile", columnNames = {"contest", "userprofile"})
})
@Subselect("select\n"
        + "    s.userprofile * 1048576 + s.contest as id,\n"
        + "    s.contest,\n"
        + "    s.userprofile,\n"
        + "    count(s.id) submit\n"
        + "from\n"
        + "    submission s\n"
        + "where\n"
        + "    userprofile is not null and\n"
        + "    problem is not null\n"
        + "group by\n"
        + "    contest, userprofile")
@Synchronize("submission")
@XmlRootElement
public class ContestUserprofileStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "contest", column = @Column(name = "contest", insertable = false, updatable = false)),
        @AttributeOverride(name = "userprofile", column = @Column(name = "userprofile", insertable = false, updatable = false))
    })
    private ContestUserprofile id;

    // TODO contest id less than 1048576 is assumed.
    @JoinColumn(name = "contest", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Contest contest;

    @JoinColumn(name = "userprofile", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Userprofile userprofile;

    @Column(name = "submit", nullable = false)
    private long submit;

}
