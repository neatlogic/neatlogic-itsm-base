package neatlogic.framework.process.dao.mapper;

import neatlogic.framework.common.dto.ValueTextVo;
import neatlogic.framework.process.dto.ChannelTypeRelationChannelVo;
import neatlogic.framework.process.dto.ChannelTypeRelationVo;
import neatlogic.framework.process.dto.ChannelTypeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Title: ChannelTypeMapper
 * @Package neatlogic.framework.process.dao.mapper
 * @Description: TODO
 * @Author: linbq
 * @Date: 2021/1/29 18:24
Copyright(c) $today.year NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
