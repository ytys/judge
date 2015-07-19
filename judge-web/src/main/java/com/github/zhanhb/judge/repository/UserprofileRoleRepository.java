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
package com.github.zhanhb.judge.repository;

import com.github.zhanhb.judge.model.Role;
import com.github.zhanhb.judge.model.Userprofile;
import com.github.zhanhb.judge.model.UserprofileRole;
import java.util.Collection;

/**
 *
 * @author zhanhb
 */
public interface UserprofileRoleRepository extends BaseRepository<UserprofileRole, Long> {

    Collection<UserprofileRole> findAllByUserprofile(Userprofile userprofile);

    public void findByUserprofileAndRole(Userprofile userprofile, Role system);

}
