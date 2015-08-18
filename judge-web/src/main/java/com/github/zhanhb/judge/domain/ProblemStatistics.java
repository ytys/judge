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
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PACKAGE)
@Subselect("select \n"
        + "    p.id as problem,\n"
        + "    count(s.id) as submit,\n"
        + "    count(t.id) as accept,\n"
        + "    count(distinct s.userprofile) as submit_user,\n"
        + "    count(distinct t.userprofile) as accept_user\n"
        + "from \n"
        + "    problem p\n"
        + "left join\n"
        + "    submission s\n"
        + "on\n"
        + "    p.id = s.problem\n"
        + "left join\n"
        + "    submission t\n"
        + "on\n"
        + "    s.id = t.id and\n"
        + "    t.judge_reply = 6\n"
        + "group by\n"
        + "    p.id")
@Synchronize({"problem", "submission"})
@XmlRootElement
public class ProblemStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "problem")
    private long id;

    @Column(name = "accept")
    private long accept;

    @Column(name = "submit")
    private long submit;

    @Column(name = "accept_user")
    private long acceptUser;

    @Column(name = "submit_user")
    private long submitUser;

}
