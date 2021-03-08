package codedriver.framework.process.dao.mapper.workcenter;

import codedriver.framework.dto.AuthorityVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.workcenter.dto.WorkcenterTheadVo;
import codedriver.framework.process.workcenter.dto.WorkcenterUserProfileVo;
import codedriver.framework.process.workcenter.dto.WorkcenterVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WorkcenterMapper {

	public Integer getWorkcenterProcessTaskCountBySql(String searchSql);

	public List<ProcessTaskVo> getWorkcenterProcessTaskIdBySql(String searchSql);

	public List<ProcessTaskVo> getWorkcenterProcessTaskInfoBySql(String searchSql);

	public List<Map<String,String>> getWorkcenterProcessTaskMapBySql(String searchSql);
	
	public List<String> getAuthorizedWorkcenterUuidList(
			@Param("userUuid")String userUuid, 
			@Param("teamUuidList")List<String> teamUuidList,
			@Param("roleUuidList")List<String> roleUuidList
			);
	
	public List<WorkcenterVo> getAuthorizedWorkcenterListByUuidList(@Param("uuidList")List<String> uuidList);
	
	public Integer checkWorkcenterNameIsRepeat(@Param("name")String workcenterName,@Param("uuid")String workcenterUuid);
	
	public List<WorkcenterVo> getWorkcenterByNameAndUuid(@Param("name")String workcenterName,@Param("uuid")String workcenterUuid);
	
	public Map<String,String> getWorkcenterConditionConfig();
	
	public List<WorkcenterTheadVo> getWorkcenterThead(WorkcenterTheadVo workcenterTheadVo);
	
	public WorkcenterUserProfileVo getWorkcenterUserProfileByUserUuid(String userUuid);
	
	public Integer deleteWorkcenterUserProfileByUserUuid(String userUuid);
	
	public Integer deleteWorkcenterByUuid(@Param("workcenterUuid")String workcenterUuid);
	
	public Integer deleteWorkcenterAuthorityByUuid(@Param("workcenterUuid")String workcenterUuid);
	
	public Integer deleteWorkcenterOwnerByUuid(@Param("workcenterUuid")String workcenterUuid);
	
	public Integer deleteWorkcenterThead(WorkcenterTheadVo workcenterTheadVo);
	
	public Integer insertWorkcenter(WorkcenterVo workcenterVo);
	
	public Integer insertWorkcenterAuthority(@Param("authorityVo")AuthorityVo authorityVo,@Param("workcenterUuid") String workcenterUuid); 
	
	public Integer insertWorkcenterOwner(@Param("userUuid")String owner,@Param("uuid")String workcenterUuid); 
	
	public Integer insertWorkcenterThead(WorkcenterTheadVo workcenterTheadVo); 
	
	public Integer insertWorkcenterUserProfile(WorkcenterUserProfileVo workcenterUserProfileVo);
	
	public Integer updateWorkcenter(WorkcenterVo workcenterVo);
	
	public Integer updateWorkcenterCondition(WorkcenterVo workcenterVo);
	
}
