package codedriver.framework.process.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import codedriver.framework.dto.AuthorityVo;
import codedriver.framework.process.dto.ChannelPriorityVo;
import codedriver.framework.process.dto.ChannelTypeVo;
import codedriver.framework.process.dto.ChannelVo;

public interface ChannelMapper {

	int searchChannelCount(ChannelVo channelVo);

	List<ChannelVo> searchChannelList(ChannelVo channelVo);

	ChannelVo getChannelByUuid(String channelUuid);
	
	List<ChannelVo> getChannelByUuidList(@Param("channelUuidList")List<String> channelUuidList);
	
	int getMaxSortByParentUuid(String parentUuid);

	List<ChannelPriorityVo> getChannelPriorityListByChannelUuid(String uuid);
	
	int checkChannelIsExists(String channelUuid);

	int checkChannelNameIsRepeat(ChannelVo channelVo);

	List<ChannelVo> getChannelListForTree(Integer isActive);

	String getProcessUuidByChannelUuid(String channelUuid);

	List<AuthorityVo> getChannelAuthorityListByChannelUuid(String uuid);
	
	int searchChannelTypeCount(ChannelTypeVo channelTypeVo);

	List<ChannelTypeVo> searchChannelTypeList(ChannelTypeVo channelTypeVo);
	
	ChannelTypeVo getChannelTypeByUuid(String uuid);

	int checkChannelTypeIsExists(String uuid);

	public int checkChannelTypeNameIsRepeat(ChannelTypeVo channelTypeVo);

	public Integer getChannelTypeMaxSort();
	
	List<String> getAuthorizedChannelUuidList(
			@Param("userUuid")String userUuid, 
			@Param("teamUuidList")List<String> teamUuidList, 
			@Param("roleNameList")List<String> roleNameList,
			@Param("channelUuid") String channelUuid
			);
	
	int replaceChannelUser(@Param("userUuid")String userUuid, @Param("channelUuid")String channelUuid);	

	int replaceChannel(ChannelVo channelVo);

	int insertChannelPriority(ChannelPriorityVo channelPriority);
	
	int replaceChannelProcess(@Param("channelUuid")String channelUuid, @Param("processUuid")String processUuid);

	int replaceChannelWorktime(@Param("channelUuid")String channelUuid, @Param("worktimeUuid")String worktimeUuid);

	int insertChannelAuthority(@Param("authorityVo")AuthorityVo authority,@Param("channelUuid") String channelUuid);
	
	int insertChannelType(ChannelTypeVo channelTypeVo);

	int updateChannelForMove(ChannelVo channelVo);

	int updateSortIncrement(@Param("parentUuid")String parentUuid, @Param("fromSort")Integer fromSort, @Param("toSort")Integer toSort);

	int updateSortDecrement(@Param("parentUuid")String parentUuid, @Param("fromSort")Integer fromSort, @Param("toSort")Integer toSort);
	
	int updateChannelTypeByUuid(ChannelTypeVo channelTypeVo);

	int deleteChannelUser(@Param("userUuid")String userUuid, @Param("channelUuid")String channelUuid);
	
	int deleteChannelByUuid(String uuid);

	int deleteChannelPriorityByChannelUuid(String channelUuid);

	int deleteChannelProcessByChannelUuid(String channelUuid);

	int deleteChannelWorktimeByChannelUuid(String channelUuid);

	int deleteChannelUserByChannelUuid(String channelUuid);

	int deleteChannelAuthorityByChannelUuid(String uuid);
	
	int deleteChannelTypeByUuid(String uuid);
}
