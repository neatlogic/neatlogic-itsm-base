/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.process.crossover;

import neatlogic.framework.crossover.ICrossoverService;
import neatlogic.framework.process.dto.ChannelTypeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IChannelTypeCrossoverMapper extends ICrossoverService {

    ChannelTypeVo getChannelTypeByUuid(String uuid);

    ChannelTypeVo getChannelTypeByName(String name);

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

    int checkChannelTypeRelationIsExists(Long id);

    List<String> getChannelTypeRelationTargetListByChannelTypeRelationId(Long channelTypeRelationId);
}
