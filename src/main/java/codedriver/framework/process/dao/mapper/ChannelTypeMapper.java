package codedriver.framework.process.dao.mapper;

import codedriver.framework.common.dto.ValueTextVo;
import codedriver.framework.process.dto.ChannelTypeRelationChannelVo;
import codedriver.framework.process.dto.ChannelTypeRelationVo;
import codedriver.framework.process.dto.ChannelTypeVo;
import codedriver.framework.process.dto.ChannelVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Title: ChannelTypeMapper
 * @Package codedriver.framework.process.dao.mapper
 * @Description: TODO
 * @Author: linbq
 * @Date: 2021/1/29 18:24
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 **/
public interface ChannelTypeMapper {

    public int searchChannelTypeCount(ChannelTypeVo channelTypeVo);

    public List<ChannelTypeVo> searchChannelTypeList(ChannelTypeVo channelTypeVo);

    public List<ValueTextVo> searchChannelTypeListForSelect(ChannelTypeVo channelTypeVo);

    public ChannelTypeVo getChannelTypeByUuid(String uuid);

    public int checkChannelTypeIsExists(String uuid);

    public int checkChannelTypeNameIsRepeat(ChannelTypeVo channelTypeVo);

    public Integer getChannelTypeMaxSort();

    public int checkChannelTypeRelationIsExists(Long id);

    public int checkChannelTypeRelationNameIsRepeat(ChannelTypeRelationVo channelTypeRelationVo);

    public ChannelTypeRelationVo getChannelTypeRelationById(Long channelTypeRelationId);

    public ChannelTypeRelationVo getChannelTypeRelationLockById(Long channelTypeRelationId);

    public List<ChannelTypeRelationVo> getChannelTypeRelationList(ChannelTypeRelationVo channelTypeRelationVo);

    public int getChannelTypeRelationCount(ChannelTypeRelationVo channelTypeRelationVo);

    public List<ValueTextVo> getChannelTypeRelationListForSelect(ChannelTypeRelationVo channelTypeRelationVo);

    public int getChannelTypeRelationCountForSelect(ChannelTypeRelationVo channelTypeRelationVo);

    public List<String> getChannelTypeRelationSourceListByChannelTypeRelationId(Long channelTypeRelationId);

    public List<String> getChannelTypeRelationTargetListByChannelTypeRelationId(Long channelTypeRelationId);

    public List<ChannelTypeRelationChannelVo>
    getChannelTypeRelationSourceListByChannelTypeRelationIdList(List<Long> channelTypeRelationIdList);

    public List<ChannelTypeRelationChannelVo> getChannelTypeRelationTargetListByChannelTypeRelationIdList(List<Long> channelTypeRelationIdList);

    public List<Long> getChannelTypeRelationIdListBySourceChannelTypeUuid(String sourceChannelTypeUuid);

    public List<Long> getAuthorizedChannelTypeRelationIdListBySourceChannelUuid(
            @Param("source") String source,
            @Param("userUuid") String userUuid,
            @Param("teamUuidList") List<String> teamUuidList,
            @Param("roleUuidList") List<String> roleUuidList,
            @Param("processUserTypeList") List<String> processUserTypeList
    );

    public List<String> getChannelUuidListByParentUuidListAndChannelTypeUuidList(
            @Param("parentUuidList") List<String> parentUuidList,
            @Param("channelTypeUuidList") List<String> channelTypeUuidList
    );

    public int getActiveChannelCountByParentUuidAndChannelTypeUuidList(
            @Param("parentUuid") String parentUuid,
            @Param("channelTypeUuidList") List<String> channelTypeUuidList
    );

//    public List<ChannelTypeRelationVo> getChannelTypeRelationReferenceCountListByChannelTypeRelationIdList(List<Long> channelTypeRelationIdList);

    public Set<String> getChannelTypeRelationReferenceUuidListByChannelTypeRelationId(Long channelTypeRelationId);

    public int checkChannelTypeRelationHasReference(Long channelTypeRelationId);

    public int checkChannelTypeHasReference(String channelTypeUuid);

    public int insertChannelType(ChannelTypeVo channelTypeVo);

    public int insertChannelTypeRelation(ChannelTypeRelationVo channelTypeRelationVo);

    public int insertChannelTypeRelationSource(
            @Param("channelTypeRelationId") Long channelTypeRelationId,
            @Param("channelTypeUuid") String channelTypeUuid
    );

    public int insertChannelTypeRelationTarget(
            @Param("channelTypeRelationId") Long channelTypeRelationId,
            @Param("channelTypeUuid") String channelTypeUuid
    );

    public int updateChannelTypeByUuid(ChannelTypeVo channelTypeVo);

    public int updateChannelTypeRelationById(ChannelTypeRelationVo channelTypeRelationVo);

    public int updateChannelTypeRelationIsActiveById(Long channelTypeRelationId);

    public int deleteChannelTypeByUuid(String uuid);

    public int deleteChannelTypeRelationById(Long channelTypeRelationId);

    public int deleteChannelTypeRelationSourceByChannelTypeRelationId(Long id);

    public int deleteChannelTypeRelationTargetByChannelTypeRelationId(Long id);
}
