/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package neatlogic.framework.process.dao.mapper.workcenter;

import neatlogic.framework.process.workcenter.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkcenterMapper {
    List<WorkcenterVo> getAllWorkcenterConditionConfig();

    List<String> getAuthorizedWorkcenterUuidList(
            @Param("userUuid") String userUuid,
            @Param("teamUuidList") List<String> teamUuidList,
            @Param("roleUuidList") List<String> roleUuidList,
            @Param("deviceType") String deviceType,
            @Param("isHasModifiedAuth") int isHasModifiedAuth,
            @Param("isHasNewTypeAuth") int isHasNewTypeAuth
    );

    List<WorkcenterVo> getAuthorizedWorkcenterListByUuidList(@Param("uuidList") List<String> uuidList);

    List<WorkcenterCatalogVo> getWorkcenterCatalogListByName(String keyword);

    Integer checkWorkcenterNameIsRepeat(@Param("name") String workcenterName, @Param("uuid") String workcenterUuid);

    WorkcenterCatalogVo getWorkcenterCatalogByName(String name);

    int checkWorkcenterCatalogNameIsRepeats(WorkcenterCatalogVo vo);

    int checkWorkcenterCatalogIsExists(Long id);

    int checkWorkcenterCatalogIsUsed(Long id);

    WorkcenterVo getWorkcenterByUuid(@Param("uuid") String workcenterUuid);

    //Map<String,String> getWorkcenterConditionConfig();

    List<WorkcenterTheadVo> getWorkcenterThead(WorkcenterTheadVo workcenterTheadVo);

    List<WorkcenterVo> getWorkcenterVoListByUuidList(@Param("uuidList") List<String> uuidList);

    List<WorkcenterAuthorityVo> getWorkcenterAuthorityVoListByUuidList(@Param("uuidList") List<String> uuidList);

    WorkcenterUserProfileVo getWorkcenterUserProfileByUserUuid(String userUuid);

    Integer deleteWorkcenterUserProfileByUserUuid(String userUuid);

    Integer deleteWorkcenterByUuid(@Param("workcenterUuid") String workcenterUuid);

    Integer deleteWorkcenterAuthorityByUuid(@Param("workcenterUuid") String workcenterUuid);

    Integer deleteWorkcenterOwnerByUuid(@Param("workcenterUuid") String workcenterUuid);

    Integer deleteWorkcenterThead(WorkcenterTheadVo workcenterTheadVo);

    void deleteWorkcenterCatalogById(Long id);

    void insertWorkcenter(WorkcenterVo workcenterVo);

    Integer insertWorkcenterAuthority(WorkcenterAuthorityVo authorityVo);

    Integer insertWorkcenterOwner(@Param("userUuid") String owner, @Param("uuid") String workcenterUuid);

    Integer insertWorkcenterThead(WorkcenterTheadVo workcenterTheadVo);

    Integer insertWorkcenterUserProfile(WorkcenterUserProfileVo workcenterUserProfileVo);

    Integer updateWorkcenter(WorkcenterVo workcenterVo);

    Integer updateWorkcenterCondition(WorkcenterVo workcenterVo);

    Integer insertWorkcenterCatalog(WorkcenterCatalogVo catalogVo);


}
