/*Copyright (C) $today.year  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.framework.process.service;

import neatlogic.framework.crossover.CrossoverServiceFactory;
import neatlogic.framework.dto.AuthorityVo;
import neatlogic.framework.process.dao.mapper.CatalogMapper;
import neatlogic.framework.process.dao.mapper.ChannelMapper;
import neatlogic.framework.process.dao.mapper.PriorityMapper;
import neatlogic.framework.process.crossover.IProcessCrossoverMapper;
import neatlogic.framework.process.dto.CatalogVo;
import neatlogic.framework.process.dto.ChannelPriorityVo;
import neatlogic.framework.process.dto.ChannelRelationVo;
import neatlogic.framework.process.dto.ChannelVo;
import neatlogic.framework.process.exception.catalog.CatalogNotFoundException;
import neatlogic.framework.process.exception.channel.ChannelNameRepeatException;
import neatlogic.framework.process.exception.channel.ChannelParentUuidCannotBeZeroException;
import neatlogic.framework.process.exception.channel.ChannelRelationSettingException;
import neatlogic.framework.process.exception.priority.PriorityNotFoundException;
import neatlogic.framework.process.exception.process.ProcessNotFoundException;
import neatlogic.framework.worktime.dao.mapper.WorktimeMapper;
import neatlogic.framework.worktime.exception.WorktimeNotFoundException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class ChannelServiceImpl implements ChannelService {

    private final static Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);

    @Resource
    private ChannelMapper channelMapper;

    @Resource
    private CatalogMapper catalogMapper;

    @Resource
    private PriorityMapper priorityMapper;

    @Resource
    private WorktimeMapper worktimeMapper;

    @Override
    public String saveChannel(ChannelVo channelVo) {
        String parentUuid = channelVo.getParentUuid();
        if (CatalogVo.ROOT_UUID.equals(parentUuid)) {
            throw new ChannelParentUuidCannotBeZeroException();
        }
        if (catalogMapper.checkCatalogIsExists(parentUuid) == 0) {
            throw new CatalogNotFoundException(parentUuid);
        }
        if (channelMapper.checkChannelNameIsRepeat(channelVo) > 0) {
            throw new ChannelNameRepeatException(channelVo.getName());
        }
        int sort;
        String uuid = channelVo.getUuid();
        ChannelVo existedChannel = channelMapper.getChannelByUuid(uuid);
        if (existedChannel == null) {//新增
            channelVo.setUuid(null);
            uuid = channelVo.getUuid();
            sort = channelMapper.getMaxSortByParentUuid(parentUuid) + 1;
        } else {//修改
            channelMapper.deleteChannelPriorityByChannelUuid(uuid);
            channelMapper.deleteChannelAuthorityByChannelUuid(uuid);
            sort = existedChannel.getSort();
        }
        channelVo.setSort(sort);
        channelMapper.replaceChannel(channelVo);
        IProcessCrossoverMapper processCrossoverMapper = CrossoverServiceFactory.getApi(IProcessCrossoverMapper.class);
        if (processCrossoverMapper.checkProcessIsExists(channelVo.getProcessUuid()) == 0) {
            throw new ProcessNotFoundException(channelVo.getProcessUuid());
        }
        channelMapper.replaceChannelProcess(uuid, channelVo.getProcessUuid());

        if (worktimeMapper.checkWorktimeIsExists(channelVo.getWorktimeUuid()) == 0) {
            throw new WorktimeNotFoundException(channelVo.getWorktimeUuid());
        }
        channelMapper.replaceChannelWorktime(uuid, channelVo.getWorktimeUuid());
        //优先级
        if(channelVo.getIsNeedPriority() == 1) {
            String defaultPriorityUuid = channelVo.getDefaultPriorityUuid();
            List<String> priorityUuidList = channelVo.getPriorityUuidList();
            for (String priorityUuid : priorityUuidList) {
                if (priorityMapper.checkPriorityIsExists(priorityUuid) == 0) {
                    throw new PriorityNotFoundException(priorityUuid);
                }
                ChannelPriorityVo channelPriority = new ChannelPriorityVo();
                channelPriority.setChannelUuid(uuid);
                channelPriority.setPriorityUuid(priorityUuid);
                if (defaultPriorityUuid.equals(priorityUuid)) {
                    channelPriority.setIsDefault(1);
                } else {
                    channelPriority.setIsDefault(0);
                }
                channelMapper.insertChannelPriority(channelPriority);
            }
        }
        List<AuthorityVo> authorityList = channelVo.getAuthorityVoList();
        if (CollectionUtils.isNotEmpty(authorityList)) {
            for (AuthorityVo authorityVo : authorityList) {
                channelMapper.insertChannelAuthority(authorityVo, channelVo.getUuid());
            }
        }
        /** 转报设置逻辑，允许转报后，转报设置必填 **/
        channelMapper.deleteChannelRelationBySource(channelVo.getUuid());
        channelMapper.deleteChannelRelationAuthorityBySource(channelVo.getUuid());
        channelMapper.deleteChannelRelationIsUsePreOwnerBySource(channelVo.getUuid());
        JSONObject config = channelVo.getConfig();
        if (MapUtils.isNotEmpty(config)) {
            Integer allowTranferReport = config.getInteger("allowTranferReport");
            if (Objects.equals(allowTranferReport, 1)) {
                JSONArray channelRelationArray = config.getJSONArray("channelRelationList");
                if (CollectionUtils.isEmpty(channelRelationArray)) {
                    throw new ChannelRelationSettingException();
                }
                List<ChannelRelationVo> channelRelationList = channelRelationArray.toJavaList(ChannelRelationVo.class);
                for (ChannelRelationVo channelRelationVo : channelRelationList) {
                    channelRelationVo.setSource(channelVo.getUuid());
                    channelMapper.insertChannelRelationIsUsePreOwner(channelRelationVo);
                    for (String typeAndtarget : channelRelationVo.getTargetList()) {
                        if (typeAndtarget.contains("#")) {
                            String[] split = typeAndtarget.split("#");
                            channelRelationVo.setType(split[0]);
                            channelRelationVo.setTarget(split[1]);
                            channelMapper.insertChannelRelation(channelRelationVo);
                        }
                    }
                    for (String authority : channelRelationVo.getAuthorityList()) {
                        if (authority.contains("#")) {
                            String[] split = authority.split("#");
                            channelRelationVo.setType(split[0]);
                            channelRelationVo.setUuid(split[1]);
                            channelMapper.insertChannelRelationAuthority(channelRelationVo);
                        }

                    }
                }
            }
        }
        return channelVo.getUuid();
    }
}
