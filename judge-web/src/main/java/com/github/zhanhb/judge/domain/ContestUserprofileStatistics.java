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
@EqualsAndHashCode(of = "id")
@Setter(AccessLevel.PACKAGE)
@Subselect("select\n"
        + "    s.contest,\n"
        + "    s.userprofile,\n"
        + "    count(s.id) as submit\n"
        + "from\n"
        + "    submission s\n"
        + "where\n"
        + "    s.contest is not null and\n"
        + "    s.userprofile is not null\n"
        + "group by\n"
        + "    s.contest, s.userprofile")
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

    @JoinColumn(name = "contest", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Contest contest;

    @JoinColumn(name = "userprofile", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Userprofile userprofile;

    @Column(name = "submit", nullable = false)
    private long submit;

}
