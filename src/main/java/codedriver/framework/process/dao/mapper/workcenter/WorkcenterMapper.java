package codedriver.framework.process.dao.mapper.workcenter;

import codedriver.framework.process.workcenter.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WorkcenterMapper {

	List<String> getAuthorizedWorkcenterUuidList(
		@Param("userUuid")String userUuid,
		@Param("teamUuidList")List<String> teamUuidList,
		@Param("roleUuidList")List<String> roleUuidList,
		@Param("deviceType") String deviceType,
		@Param("isHasModifiedAuth") int isHasModifiedAuth,
		@Param("isHasNewTypeAuth") int isHasNewTypeAuth
		);
	
	List<WorkcenterVo> getAuthorizedWorkcenterListByUuidList(@Param("uuidList")List<String> uuidList);

	List<WorkcenterCatalogVo> getWorkcenterCatalogListByName(String name);

	Integer checkWorkcenterNameIsRepeat(@Param("name")String workcenterName,@Param("uuid")String workcenterUuid);

	Integer checkWorkcenterCatalogNameIsRepeats(WorkcenterCatalogVo vo);

	Integer checkWorkcenterCatalogIsExists(Long id);

	WorkcenterVo getWorkcenterByUuid(@Param("uuid")String workcenterUuid);
	
	Map<String,String> getWorkcenterConditionConfig();
	
	List<WorkcenterTheadVo> getWorkcenterThead(WorkcenterTheadVo workcenterTheadVo);

	List<WorkcenterVo> getWorkcenterVoListByUuidList(@Param("uuidList") List<String> uuidList);

	List<WorkcenterAuthorityVo> getWorkcenterAuthorityVoListByUuidList(@Param("uuidList") List<String> uuidList);
	
	WorkcenterUserProfileVo getWorkcenterUserProfileByUserUuid(String userUuid);
	
	Integer deleteWorkcenterUserProfileByUserUuid(String userUuid);
	
	Integer deleteWorkcenterByUuid(@Param("workcenterUuid")String workcenterUuid);
	
	Integer deleteWorkcenterAuthorityByUuid(@Param("workcenterUuid")String workcenterUuid);
	
	Integer deleteWorkcenterOwnerByUuid(@Param("workcenterUuid")String workcenterUuid);
	
	Integer deleteWorkcenterThead(WorkcenterTheadVo workcenterTheadVo);

	Integer deleteWorkcenterCatalogById(Long id);

	Integer replaceWorkcenter(WorkcenterVo workcenterVo);
	
	Integer insertWorkcenterAuthority(WorkcenterAuthorityVo authorityVo);
	
	Integer insertWorkcenterOwner(@Param("userUuid")String owner,@Param("uuid")String workcenterUuid);
	
	Integer insertWorkcenterThead(WorkcenterTheadVo workcenterTheadVo);
	
	Integer insertWorkcenterUserProfile(WorkcenterUserProfileVo workcenterUserProfileVo);
	
	Integer updateWorkcenter(WorkcenterVo workcenterVo);
	
	Integer updateWorkcenterCondition(WorkcenterVo workcenterVo);

	Integer updateWorkcenterCatalog(WorkcenterCatalogVo catalogVo);

	Integer insertWorkcenterCatalog(WorkcenterCatalogVo catalogVo);
}
