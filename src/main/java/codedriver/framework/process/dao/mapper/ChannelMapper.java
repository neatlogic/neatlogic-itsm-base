package codedriver.framework.process.dao.mapper;

import java.util.List;

import codedriver.framework.common.dto.ValueTextVo;
import org.apache.ibatis.annotations.Param;

import codedriver.framework.dto.AuthorityVo;
import codedriver.framework.process.dto.ChannelPriorityVo;
import codedriver.framework.process.dto.ChannelRelationVo;
import codedriver.framework.process.dto.ChannelTypeRelationChannelVo;
import codedriver.framework.process.dto.ChannelTypeRelationVo;
import codedriver.framework.process.dto.ChannelTypeVo;
import codedriver.framework.process.dto.ChannelVo;

public interface ChannelMapper {

	public int searchChannelCount(ChannelVo channelVo);

	public List<ChannelVo> searchChannelList(ChannelVo channelVo);

	public List<ValueTextVo> searchChannelListForSelect(ChannelVo channelVo);

	public ChannelVo getChannelByUuid(String channelUuid);
	
	public ChannelVo getChannelByName(String channelName);
	
	public List<ChannelVo> getChannelByUuidList(@Param("channelUuidList")List<String> channelUuidList);
	
	public int getMaxSortByParentUuid(String parentUuid);

	public List<ChannelPriorityVo> getChannelPriorityListByChannelUuid(String uuid);
	
	public int checkChannelIsExists(String channelUuid);

	public int checkChannelNameIsRepeat(ChannelVo channelVo);

	public List<ChannelVo> getChannelListForTree(Integer isActive);

	public String getProcessUuidByChannelUuid(String channelUuid);

	public List<AuthorityVo> getChannelAuthorityListByChannelUuid(String uuid);
	
	public int searchChannelTypeCount(ChannelTypeVo channelTypeVo);

	public List<ChannelTypeVo> searchChannelTypeList(ChannelTypeVo channelTypeVo);

	public List<ValueTextVo> searchChannelTypeListForSelect(ChannelTypeVo channelTypeVo);

	public ChannelTypeVo getChannelTypeByUuid(String uuid);

	public int checkChannelTypeIsExists(String uuid);

	public int checkChannelTypeNameIsRepeat(ChannelTypeVo channelTypeVo);

	public Integer getChannelTypeMaxSort();
	
	public List<String> getAuthorizedChannelUuidList(
			@Param("userUuid")String userUuid, 
			@Param("teamUuidList")List<String> teamUuidList, 
			@Param("roleUuidList")List<String> roleUuidList,
			@Param("channelUuid") String channelUuid
			);
	
	public List<ChannelVo> getAuthorizedChannelListByParentUuid(
			@Param("userUuid")String userUuid, 
			@Param("teamUuidList")List<String> teamUuidList, 
			@Param("roleUuidList")List<String> roleUuidList,
			@Param("parentUuid") String parentUuid
			);
	
	public List<String> getAllAncestorNameListByParentUuid(String parentUuid);

    public int checkChannelIsFavorite(@Param("userUuid")String userUuid, @Param("channelUuid")String channelUuid);

    public int checkChannelTypeRelationIsExists(Long id);

    public int checkChannelTypeRelationNameIsRepeat(ChannelTypeRelationVo channelTypeRelationVo);

    public ChannelTypeRelationVo getChannelTypeRelationById(Long channelTypeRelationId);

    public List<ChannelTypeRelationVo> getChannelTypeRelationList(ChannelTypeRelationVo channelTypeRelationVo);

    public int getChannelTypeRelationCount(ChannelTypeRelationVo channelTypeRelationVo);

    public List<ValueTextVo> getChannelTypeRelationListForSelect(ChannelTypeRelationVo channelTypeRelationVo);
    
    public int getChannelTypeRelationCountForSelect(ChannelTypeRelationVo channelTypeRelationVo);

    public List<String> getChannelTypeRelationSourceListByChannelTypeRelationId(Long channelTypeRelationId);

    public List<String> getChannelTypeRelationTargetListByChannelTypeRelationId(Long channelTypeRelationId);

    public List<ChannelRelationVo> getChannelRelationListBySource(String channelUuid);

    public List<ChannelRelationVo> getChannelRelationAuthorityListBySource(String channelUuid);

    public List<ChannelVo> getChannelListByChannelTypeUuidList(List<String> channelTypeUuidList);

    public List<ChannelTypeRelationChannelVo>
        getChannelTypeRelationSourceListByChannelTypeRelationIdList(List<Long> channelTypeRelationIdList);

    public List<ChannelTypeRelationChannelVo>
        getChannelTypeRelationTargetListByChannelTypeRelationIdList(List<Long> channelTypeRelationIdList);

    public List<Long> getChannelTypeRelationIdListBySourceChannelTypeUuid(String sourceChannelTypeUuid);

    public List<Long> getAuthorizedChannelTypeRelationIdListBySourceChannelUuid(
        @Param("source") String source,
        @Param("userUuid") String userUuid, 
        @Param("teamUuidList") List<String> teamUuidList, 
        @Param("roleUuidList") List<String> roleUuidList
    );

    public List<ChannelRelationVo> getChannelRelationTargetList(ChannelRelationVo channelRelationVo);
	
	public int replaceChannelUser(@Param("userUuid")String userUuid, @Param("channelUuid")String channelUuid);	

	public int replaceChannel(ChannelVo channelVo);

	public int insertChannelPriority(ChannelPriorityVo channelPriority);
	
	public int replaceChannelProcess(@Param("channelUuid")String channelUuid, @Param("processUuid")String processUuid);

	public int replaceChannelWorktime(@Param("channelUuid")String channelUuid, @Param("worktimeUuid")String worktimeUuid);

	public int insertChannelAuthority(@Param("authorityVo")AuthorityVo authority,@Param("channelUuid") String channelUuid);
	
	public int insertChannelType(ChannelTypeVo channelTypeVo);

    public int insertChannelTypeRelation(ChannelTypeRelationVo channelTypeRelationVo);

    public int insertChannelTypeRelationSource(@Param("channelTypeRelationId")Long channelTypeRelationId, @Param("channelTypeUuid")String channelTypeUuid);

    public int insertChannelTypeRelationTarget(@Param("channelTypeRelationId")Long channelTypeRelationId, @Param("channelTypeUuid")String channelTypeUuid);

    public int insertChannelRelation(ChannelRelationVo channelRelationVo);

    public int insertChannelRelationAuthority(ChannelRelationVo channelRelationVo);

	public int updateChannelForMove(ChannelVo channelVo);

	public int updateSortIncrement(@Param("parentUuid")String parentUuid, @Param("fromSort")Integer fromSort, @Param("toSort")Integer toSort);

	public int updateSortDecrement(@Param("parentUuid")String parentUuid, @Param("fromSort")Integer fromSort, @Param("toSort")Integer toSort);
	
	public int updateChannelTypeByUuid(ChannelTypeVo channelTypeVo);

    public int updateChannelTypeRelationById(ChannelTypeRelationVo channelTypeRelationVo);

	public int deleteChannelUser(@Param("userUuid")String userUuid, @Param("channelUuid")String channelUuid);
	
	public int deleteChannelByUuid(String uuid);

	public int deleteChannelPriorityByChannelUuid(String channelUuid);

	public int deleteChannelProcessByChannelUuid(String channelUuid);

	public int deleteChannelWorktimeByChannelUuid(String channelUuid);

	public int deleteChannelUserByChannelUuid(String channelUuid);

	public int deleteChannelAuthorityByChannelUuid(String uuid);
	
	public int deleteChannelTypeByUuid(String uuid);
	
	public int deleteChannelTypeRelationById(Long channelTypeRelationId);

    public int deleteChannelTypeRelationSourceByChannelTypeRelationId(Long id);

    public int deleteChannelTypeRelationTargetByChannelTypeRelationId(Long id);

    public int deleteChannelRelationBySource(String channelUuid);

    public int deleteChannelRelationAuthorityBySource(String channelUuid);
}
