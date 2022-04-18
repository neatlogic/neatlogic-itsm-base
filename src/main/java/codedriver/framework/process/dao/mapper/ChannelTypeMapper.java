package codedriver.framework.process.dao.mapper;

import codedriver.framework.common.dto.ValueTextVo;
import codedriver.framework.process.dto.ChannelTypeRelationChannelVo;
import codedriver.framework.process.dto.ChannelTypeRelationVo;
import codedriver.framework.process.dto.ChannelTypeVo;
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

    int searchChannelTypeCount(ChannelTypeVo channelTypeVo);

    List<ChannelTypeVo> searchChannelTypeList(ChannelTypeVo channelTypeVo);

    List<ValueTextVo> searchChannelTypeListForSelect(ChannelTypeVo channelTypeVo);

    ChannelTypeVo getChannelTypeByUuid(String uuid);

    List<ChannelTypeVo> getChannelTypeByUuidList(List<String> uuidList);

    int checkChannelTypeIsExists(String uuid);

    int checkChannelTypeNameIsRepeat(ChannelTypeVo channelTypeVo);

    Integer getChannelTypeMaxSort();

    int checkChannelTypeRelationIsExists(Long id);

    int checkChannelTypeRelationNameIsRepeat(ChannelTypeRelationVo channelTypeRelationVo);

    ChannelTypeRelationVo getChannelTypeRelationById(Long channelTypeRelationId);

    ChannelTypeRelationVo getChannelTypeRelationLockById(Long channelTypeRelationId);

    List<ChannelTypeRelationVo> getChannelTypeRelationList(ChannelTypeRelationVo channelTypeRelationVo);

    int getChannelTypeRelationCount(ChannelTypeRelationVo channelTypeRelationVo);

    List<ValueTextVo> getChannelTypeRelationListForSelect(ChannelTypeRelationVo channelTypeRelationVo);

    int getChannelTypeRelationCountForSelect(ChannelTypeRelationVo channelTypeRelationVo);

    List<String> getChannelTypeRelationSourceListByChannelTypeRelationId(Long channelTypeRelationId);

    List<String> getChannelTypeRelationTargetListByChannelTypeRelationId(Long channelTypeRelationId);

    List<ChannelTypeRelationChannelVo>
    getChannelTypeRelationSourceListByChannelTypeRelationIdList(List<Long> channelTypeRelationIdList);

    List<ChannelTypeRelationChannelVo> getChannelTypeRelationTargetListByChannelTypeRelationIdList(List<Long> channelTypeRelationIdList);

    List<Long> getChannelTypeRelationIdListBySourceChannelTypeUuid(String sourceChannelTypeUuid);

    List<Long> getAuthorizedChannelTypeRelationIdListBySourceChannelUuid(
            @Param("source") String source,
            @Param("userUuid") String userUuid,
            @Param("teamUuidList") List<String> teamUuidList,
            @Param("roleUuidList") List<String> roleUuidList,
            @Param("processUserTypeList") List<String> processUserTypeList
    );

    List<String> getChannelUuidListByParentUuidListAndChannelTypeUuidList(
            @Param("parentUuidList") List<String> parentUuidList,
            @Param("channelTypeUuidList") List<String> channelTypeUuidList
    );

    int getActiveChannelCountByParentUuidAndChannelTypeUuidList(
            @Param("parentUuid") String parentUuid,
            @Param("channelTypeUuidList") List<String> channelTypeUuidList
    );

    Long checkChannelTypeRelationIsUsedByChannelTypeRelationId(Long channelTypeRelationId);

    Set<String> getChannelTypeRelationReferenceUuidListByChannelTypeRelationId(Long channelTypeRelationId);

    int checkChannelTypeRelationHasReference(Long channelTypeRelationId);

    int checkChannelTypeHasReference(String channelTypeUuid);

    ChannelTypeVo getChannelTypeByName(String name);

    int insertChannelType(ChannelTypeVo channelTypeVo);

    int insertChannelTypeRelation(ChannelTypeRelationVo channelTypeRelationVo);

    int insertChannelTypeRelationSource(
            @Param("channelTypeRelationId") Long channelTypeRelationId,
            @Param("channelTypeUuid") String channelTypeUuid
    );

    int insertChannelTypeRelationTarget(
            @Param("channelTypeRelationId") Long channelTypeRelationId,
            @Param("channelTypeUuid") String channelTypeUuid
    );

    int updateChannelTypeByUuid(ChannelTypeVo channelTypeVo);

    int updateChannelTypeRelationById(ChannelTypeRelationVo channelTypeRelationVo);

    int updateChannelTypeRelationIsActiveById(Long channelTypeRelationId);

    int updateChannelTypeRelationToDeleteById(Long channelTypeRelationId);

    int deleteChannelTypeByUuid(String uuid);

    int deleteChannelTypeRelationById(Long channelTypeRelationId);

    int deleteChannelTypeRelationSourceByChannelTypeRelationId(Long id);

    int deleteChannelTypeRelationTargetByChannelTypeRelationId(Long id);
}
