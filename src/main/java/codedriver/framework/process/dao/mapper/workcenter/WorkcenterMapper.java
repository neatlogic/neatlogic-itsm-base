package codedriver.framework.process.dao.mapper.workcenter;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import codedriver.framework.process.dto.AuthorityVo;
import codedriver.framework.process.workcenter.dto.WorkcenterTheadVo;
import codedriver.framework.process.workcenter.dto.WorkcenterVo;

public interface WorkcenterMapper {
	
	List<WorkcenterVo> getAuthorizedWorkcenterList(
			@Param("userId")String userId, 
			@Param("teamUuidList")List<String> teamUuidList,
			@Param("roleNameList")List<String> roleNameList
			);
	
	public Integer checkWorkcenterNameIsRepeat(@Param("name")String workcenterName,@Param("uuid")String workcenterUuid);
	
	public List<WorkcenterVo> getWorkcenterByNameAndUuid(@Param("name")String workcenterName,@Param("uuid")String workcenterUuid);
	
	public Map<String,String> getWorkcenterConditionConfig();
	
	public List<WorkcenterTheadVo> getWorkcenterThead(WorkcenterTheadVo workcenterTheadVo);
	
	public Integer deleteWorkcenterByUuid(@Param("workcenterUuid")String workcenterUuid);
	
	public Integer deleteWorkcenterAuthorityByUuid(@Param("workcenterUuid")String workcenterUuid);
	
	public Integer deleteWorkcenterOwnerByUuid(@Param("workcenterUuid")String workcenterUuid);
	
	public Integer deleteWorkcenterThead(WorkcenterTheadVo workcenterTheadVo);
	
	public Integer insertWorkcenter(WorkcenterVo workcenterVo);
	
	public Integer insertWorkcenterAuthority(@Param("authorityVo")AuthorityVo authorityVo,@Param("workcenterUuid") String workcenterUuid); 
	
	public Integer insertWorkcenterOwner(@Param("userId")String owner,@Param("uuid")String workcenterUuid); 
	
	public Integer insertWorkcenterThead(WorkcenterTheadVo workcenterTheadVo); 
	
	public Integer updateWorkcenter(WorkcenterVo workcenterVo);
}
