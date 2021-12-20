package codedriver.framework.process.dao.mapper.workcenter;

import codedriver.framework.dto.AuthorityVo;
import codedriver.framework.process.workcenter.dto.WorkcenterTheadVo;
import codedriver.framework.process.workcenter.dto.WorkcenterUserProfileVo;
import codedriver.framework.process.workcenter.dto.WorkcenterVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WorkcenterMapper {

	List<String> getAuthorizedWorkcenterUuidList(
		@Param("userUuid")String userUuid,
		@Param("teamUuidList")List<String> teamUuidList,
		@Param("roleUuidList")List<String> roleUuidList,
		@Param("deviceType") String deviceType,
		@Param("isHasModifiedAuth") int isHasModifiedAuth
		);
	
	List<WorkcenterVo> getAuthorizedWorkcenterListByUuidList(@Param("uuidList")List<String> uuidList);
	
	Integer checkWorkcenterNameIsRepeat(@Param("name")String workcenterName,@Param("uuid")String workcenterUuid);
	
	List<WorkcenterVo> getWorkcenterByNameAndUuid(@Param("name")String workcenterName,@Param("uuid")String workcenterUuid);
	
	Map<String,String> getWorkcenterConditionConfig();
	
	List<WorkcenterTheadVo> getWorkcenterThead(WorkcenterTheadVo workcenterTheadVo);

	List<WorkcenterVo> getWorkcenterVoListByUuidList(@Param("uuidList") List<String> uuidList);
	
	WorkcenterUserProfileVo getWorkcenterUserProfileByUserUuid(String userUuid);
	
	Integer deleteWorkcenterUserProfileByUserUuid(String userUuid);
	
	Integer deleteWorkcenterByUuid(@Param("workcenterUuid")String workcenterUuid);
	
	Integer deleteWorkcenterAuthorityByUuid(@Param("workcenterUuid")String workcenterUuid);
	
	Integer deleteWorkcenterOwnerByUuid(@Param("workcenterUuid")String workcenterUuid);
	
	Integer deleteWorkcenterThead(WorkcenterTheadVo workcenterTheadVo);
	
	Integer insertWorkcenter(WorkcenterVo workcenterVo);
	
	Integer insertWorkcenterAuthority(@Param("authorityVo")AuthorityVo authorityVo,@Param("workcenterUuid") String workcenterUuid);
	
	Integer insertWorkcenterOwner(@Param("userUuid")String owner,@Param("uuid")String workcenterUuid);
	
	Integer insertWorkcenterThead(WorkcenterTheadVo workcenterTheadVo);
	
	Integer insertWorkcenterUserProfile(WorkcenterUserProfileVo workcenterUserProfileVo);
	
	Integer updateWorkcenter(WorkcenterVo workcenterVo);
	
	Integer updateWorkcenterCondition(WorkcenterVo workcenterVo);
	
}
