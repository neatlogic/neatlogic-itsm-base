/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.service;

import codedriver.framework.process.dao.mapper.CatalogMapper;
import codedriver.framework.process.dao.mapper.ChannelMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskAgentMapper;
import codedriver.framework.process.dto.CatalogVo;
import codedriver.framework.process.dto.ChannelVo;
import codedriver.framework.process.dto.agent.ProcessTaskAgentTargetVo;
import codedriver.framework.process.dto.agent.ProcessTaskAgentVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author linbq
 * @since 2021/10/11 15:51
 **/
@Service
public class ProcessTaskAgentServiceImpl implements ProcessTaskAgentService {

    @Resource
    private ProcessTaskAgentMapper processTaskAgentMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private CatalogMapper catalogMapper;

    @Override
    public List<String> getFromUserUuidListByToUserUuidAndChannelUuid(String toUserUuid, String channelUuid) {
        List<String> fromUserUuidList = new ArrayList<>();
        List<ProcessTaskAgentVo> processTaskAgentList = processTaskAgentMapper.getProcessTaskAgentListByToUserUuid(toUserUuid);
        for (ProcessTaskAgentVo processTaskAgentVo : processTaskAgentList) {
            String fromUserUuid = processTaskAgentVo.getFromUserUuid();
            if (fromUserUuidList.contains(fromUserUuid)) {
                continue;
            }
            boolean flag = false;
            List<String> catalogUuidList = new ArrayList<>();
            List<ProcessTaskAgentTargetVo> processTaskAgentTargetList = processTaskAgentMapper.getProcessTaskAgentTargetListByProcessTaskAgentId(processTaskAgentVo.getId());
            for (ProcessTaskAgentTargetVo processTaskAgentTargetVo : processTaskAgentTargetList) {
                String type = processTaskAgentTargetVo.getType();
                if ("channel".equals(type)) {
                    if (channelUuid.equals(processTaskAgentTargetVo.getTarget())) {
                        flag = true;
                        break;
                    }
                } else if ("catalog".equals(type)) {
                    catalogUuidList.add(processTaskAgentTargetVo.getTarget());
                }
            }
            if (!flag && CollectionUtils.isNotEmpty(catalogUuidList)) {
                ChannelVo channelVo = channelMapper.getChannelByUuid(channelUuid);
                CatalogVo catalogVo = catalogMapper.getCatalogByUuid(channelVo.getParentUuid());
                List<String> upwardUuidList = catalogMapper.getUpwardUuidListByLftRht(catalogVo.getLft(), catalogVo.getRht());
                flag = catalogUuidList.removeAll(upwardUuidList);
            }
            if (flag) {
                fromUserUuidList.add(fromUserUuid);
            }
        }
        return fromUserUuidList;
    }

    @Override
    public List<String> getChannelUuidListByProcessTaskAgentId(Long processTaskAgentId) {
        List<String> resultList = new ArrayList<>();
        Set<String> catalogUuidList = new HashSet<>();
        List<ProcessTaskAgentTargetVo> processTaskAgentTargetList = processTaskAgentMapper.getProcessTaskAgentTargetListByProcessTaskAgentId(processTaskAgentId);
        for (ProcessTaskAgentTargetVo processTaskAgentTargetVo : processTaskAgentTargetList) {
            String type = processTaskAgentTargetVo.getType();
            if ("channel".equals(type)) {
                if (channelMapper.checkChannelIsExists(processTaskAgentTargetVo.getTarget()) > 0) {
                    resultList.add(processTaskAgentTargetVo.getTarget());
                }
            } else if ("catalog".equals(type)) {
                CatalogVo catalogVo = catalogMapper.getCatalogByUuid(processTaskAgentTargetVo.getTarget());
                if (catalogVo != null) {
                    List<String> downwardUuidList = catalogMapper.getDownwardUuidListByLftRht(catalogVo.getLft(), catalogVo.getRht());
                    catalogUuidList.addAll(downwardUuidList);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(catalogUuidList)) {
            List<String> channelUuidList = channelMapper.getChannelUuidListByParentUuidList(new ArrayList<>(catalogUuidList));
            if (CollectionUtils.isNotEmpty(channelUuidList)) {
                channelUuidList.removeAll(resultList);
                resultList.addAll(channelUuidList);
            }
        }
        return resultList;
    }
}
