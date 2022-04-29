/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dao.mapper.workcenter;

import codedriver.framework.process.workcenter.dto.*;
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
