/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dao.mapper;

import codedriver.framework.common.dto.ValueTextVo;
import codedriver.framework.dto.AuthorityVo;
import codedriver.framework.form.dto.FormAttributeVo;
import codedriver.framework.form.dto.FormVersionVo;
import codedriver.framework.process.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChannelMapper {

    public int searchChannelCount(ChannelVo channelVo);

    public List<ChannelVo> searchChannelList(ChannelVo channelVo);

    List<ChannelVo> getAllChannelList();

    public List<ValueTextVo> searchChannelListForSelect(ChannelVo channelVo);

    public ChannelVo getChannelByUuid(String channelUuid);

    public ChannelVo getChannelByName(String channelName);

    public List<ChannelVo> getChannelByUuidList(@Param("channelUuidList") List<String> channelUuidList);

    public List<ChannelVo> getChannelVoByUuidList(List<String> uuidList);

    List<String> getChannelUuidListByParentUuidList(List<String> parentUuidList);

    public List<ChannelVo> getAllChannelPriorityList();

    public int getMaxSortByParentUuid(String parentUuid);

    public List<ChannelPriorityVo> getChannelPriorityListByChannelUuid(String uuid);

    public int checkChannelIsExists(String channelUuid);

    public int checkChannelNameIsRepeat(ChannelVo channelVo);

    public List<ChannelVo> getChannelListForTree(Integer isActive);

    public String getProcessUuidByChannelUuid(String channelUuid);

    public String getWorktimeUuidByChannelUuid(String channelUuid);

    public List<AuthorityVo> getChannelAuthorityListByChannelUuid(String uuid);

    public List<String> getAuthorizedChannelUuidList(@Param("userUuid") String userUuid,
                                                     @Param("teamUuidList") List<String> teamUuidList, @Param("roleUuidList") List<String> roleUuidList,
                                                     @Param("channelUuid") String channelUuid);

    List<String> getActiveAuthorizedChannelUuidList(@Param("userUuid") String userUuid,
                                                    @Param("teamUuidList") List<String> teamUuidList, @Param("roleUuidList") List<String> roleUuidList,
                                                    @Param("channelUuid") String channelUuid);

    public List<ChannelVo> getAuthorizedChannelListByParentUuid(@Param("userUuid") String userUuid,
                                                                @Param("teamUuidList") List<String> teamUuidList, @Param("roleUuidList") List<String> roleUuidList,
                                                                @Param("parentUuid") String parentUuid);

    public List<String> getAllAncestorNameListByParentUuid(String parentUuid);

    public int checkChannelIsFavorite(@Param("userUuid") String userUuid, @Param("channelUuid") String channelUuid);

    List<ChannelVo> getChannelListByChannelTypeUuidList(List<String> channelTypeUuidList);

    List<ChannelRelationVo> getChannelRelationListBySource(String channelUuid);

    List<ChannelRelationVo> getChannelRelationAuthorityListBySource(String channelUuid);

    List<ChannelRelationVo> getChannelRelationTargetList(ChannelRelationVo channelRelationVo);

    List<ChannelVo> getFavoriteChannelList(ChannelVo channelVo);

    FormVersionVo getFormVersionByChannelUuid(String channelUuid);

    List<FormAttributeVo> getFormAttributeByChannelUuid(String channelUuid);

    List<String> getFormUuidListByChannelUuidList(List<String> channelUuidList);

    Integer getChannelRelationIsUsePreOwnerBySourceAndChannelTypeRelationId(ChannelRelationVo channelRelationVo);

    int replaceChannelUser(@Param("userUuid") String userUuid, @Param("channelUuid") String channelUuid);

    int replaceChannel(ChannelVo channelVo);

    int insertChannelPriority(ChannelPriorityVo channelPriority);

    int replaceChannelProcess(@Param("channelUuid") String channelUuid,
                              @Param("processUuid") String processUuid);

    int replaceChannelWorktime(@Param("channelUuid") String channelUuid,
                               @Param("worktimeUuid") String worktimeUuid);

    int insertChannelAuthority(@Param("authorityVo") AuthorityVo authority,
                               @Param("channelUuid") String channelUuid);

    int insertChannelRelation(ChannelRelationVo channelRelationVo);

    int insertChannelRelationAuthority(ChannelRelationVo channelRelationVo);

    int insertChannelRelationIsUsePreOwner(ChannelRelationVo channelRelationVo);

    int updateChannelForMove(ChannelVo channelVo);

    int updateSortIncrement(@Param("parentUuid") String parentUuid, @Param("fromSort") Integer fromSort,
                            @Param("toSort") Integer toSort);

    int updateSortDecrement(@Param("parentUuid") String parentUuid, @Param("fromSort") Integer fromSort,
                            @Param("toSort") Integer toSort);

    int updateChannelConfig(ChannelVo channelVo);

    int deleteChannelUser(@Param("userUuid") String userUuid, @Param("channelUuid") String channelUuid);

    int deleteChannelByUuid(String uuid);

    int deleteChannelPriorityByChannelUuid(String channelUuid);

    int deleteChannelProcessByChannelUuid(String channelUuid);

    int deleteChannelWorktimeByChannelUuid(String channelUuid);

    int deleteChannelUserByChannelUuid(String channelUuid);

    int deleteChannelAuthorityByChannelUuid(String uuid);

    int deleteChannelRelationBySource(String channelUuid);

    int deleteChannelRelationAuthorityBySource(String channelUuid);

    int deleteChannelRelationIsUsePreOwnerBySource(String channelUuid);
}
