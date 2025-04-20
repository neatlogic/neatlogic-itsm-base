/*
 * Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package neatlogic.framework.process.crossover;

import neatlogic.framework.crossover.ICrossoverService;
import neatlogic.framework.process.dto.ChannelRelationVo;
import neatlogic.framework.process.dto.ChannelVo;

import java.util.List;

public interface IChannelCrossoverMapper extends ICrossoverService {

    ChannelVo getChannelByUuid(String channelUuid);

    ChannelVo getChannelByName(String channelName);

    String getWorktimeUuidByChannelUuid(String channelUuid);

    String getProcessUuidByChannelUuid(String channelUuid);

    int checkChannelNameIsRepeat(ChannelVo channelVo);

    List<ChannelRelationVo> getChannelRelationTargetList(ChannelRelationVo channelRelationVo);

    List<ChannelRelationVo> getChannelRelationListBySource(String channelUuid);

    int searchChannelCount(ChannelVo channelVo);

    List<ChannelVo> searchChannelList(ChannelVo channelVo);
}
