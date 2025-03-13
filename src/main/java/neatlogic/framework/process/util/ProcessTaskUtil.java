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

package neatlogic.framework.process.util;

import com.alibaba.fastjson.JSONArray;
import neatlogic.framework.process.constvalue.ProcessFlowDirection;
import neatlogic.framework.process.dto.ProcessStepRelVo;
import neatlogic.framework.process.dto.ProcessTaskConvergeVo;
import neatlogic.framework.process.dto.ProcessTaskStepRelVo;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.util.*;

public class ProcessTaskUtil {
    
    private final static Logger logger = LoggerFactory.getLogger(ProcessTaskUtil.class);

    public static List<Long> getEffectivePostStepIdList(Long startStepId, Long endStepId, List<ProcessTaskStepRelVo> allProcessTaskStepRelList, @Nullable List<ProcessTaskConvergeVo> allProcessTaskConvergeList) {
        return getEffectivePostStepIdList(startStepId, endStepId, allProcessTaskStepRelList, allProcessTaskConvergeList, null);
    }

    public static List<Long> getEffectivePostStepIdList(Long startStepId, Long endStepId, List<ProcessTaskStepRelVo> allProcessTaskStepRelList) {
        return getEffectivePostStepIdList(startStepId, endStepId, allProcessTaskStepRelList, null, null);
    }

    public static List<String> getEffectivePostStepUuidList(String startStepUuid, String endStepUuid, List<ProcessStepRelVo> allProcessStepRelList) {
        return getEffectivePostStepUuidList(startStepUuid, endStepUuid, allProcessStepRelList, null, null);
    }

    public static List<Long> getEffectivePostStepIdList(Long startStepId, Long endStepId, List<ProcessTaskStepRelVo> allProcessTaskStepRelList, @Nullable List<ProcessTaskConvergeVo> allProcessTaskConvergeList, List<ProcessTaskStepVo> allProcessTaskStepList) {
        Map<Long, String> id2UuidMap = new HashMap<>();
        Map<String, Long> uuid2IdMap = new HashMap<>();
        List<ProcessStepRelVo> allProcessStepRelList = new ArrayList<>();
        for (ProcessTaskStepRelVo processTaskStepRelVo : allProcessTaskStepRelList) {
            ProcessStepRelVo processStepRelVo = new ProcessStepRelVo();
            processStepRelVo.setFromStepUuid(processTaskStepRelVo.getFromProcessStepUuid());
            processStepRelVo.setToStepUuid(processTaskStepRelVo.getToProcessStepUuid());
            processStepRelVo.setType(processTaskStepRelVo.getType());
            processStepRelVo.setName(processTaskStepRelVo.getName());
            allProcessStepRelList.add(processStepRelVo);
            id2UuidMap.put(processTaskStepRelVo.getFromProcessTaskStepId(), processTaskStepRelVo.getFromProcessStepUuid());
            id2UuidMap.put(processTaskStepRelVo.getToProcessTaskStepId(), processTaskStepRelVo.getToProcessStepUuid());
            uuid2IdMap.put(processTaskStepRelVo.getFromProcessStepUuid(), processTaskStepRelVo.getFromProcessTaskStepId());
            uuid2IdMap.put(processTaskStepRelVo.getToProcessStepUuid(), processTaskStepRelVo.getToProcessTaskStepId());
        }
        List<Map<String, String>> allProcessConvergeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(allProcessTaskConvergeList)) {
            for (ProcessTaskConvergeVo processTaskConvergeVo : allProcessTaskConvergeList) {
                Long convergeId = processTaskConvergeVo.getConvergeId();
                Long processTaskStepId = processTaskConvergeVo.getProcessTaskStepId();
                Map<String, String> map = new HashMap<>();
                map.put("convergeUuid", id2UuidMap.get(convergeId));
                map.put("processStepUuid", id2UuidMap.get(processTaskStepId));
                allProcessConvergeList.add(map);
            }
        }
        Map<String, String> uuid2NameMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(allProcessTaskStepList)) {
            for (ProcessTaskStepVo processTaskStepVo : allProcessTaskStepList) {
                if (StringUtils.isNotBlank(processTaskStepVo.getProcessStepUuid()) && StringUtils.isNotBlank(processTaskStepVo.getName())) {
                    uuid2NameMap.put(processTaskStepVo.getProcessStepUuid(), processTaskStepVo.getName());
                }
            }
        }
        String startStepUuid = id2UuidMap.get(startStepId);
        String endStepUuid = id2UuidMap.get(endStepId);
        List<String> effectiveStepUuidList = getEffectivePostStepUuidList(startStepUuid, endStepUuid, allProcessStepRelList, allProcessConvergeList, uuid2NameMap);
        List<Long> idList = new ArrayList<>(effectiveStepUuidList.size());
        for (String uuid : effectiveStepUuidList) {
            Long id = uuid2IdMap.get(uuid);
            if (id != null) {
                idList.add(id);
            }
        }
        return idList;
    }

    /**
     * 通过深度优先算法遍历流程图所有节点，找出开始节点的所有有效后置节点UUID列表
     * @param startStepUuid 开始节点UUID
     * @param endStepUuid 结束节点UUID
     * @param allProcessStepRelList 流程图所有连线列表
     * @param allProcessConvergeList
     * @param uuid2NameMap 节点UUID与名称键值对
     * @return 返回所有有效后置节点UUID列表
     */
    public static List<String> getEffectivePostStepUuidList(String startStepUuid, String endStepUuid, List<ProcessStepRelVo> allProcessStepRelList, @Nullable List<Map<String, String>> allProcessConvergeList, @Nullable Map<String, String> uuid2NameMap) {
        List<String> effectiveStepUuidList = new ArrayList<>();
        int count = 0;
        int stackSize = 0;
        Stack<StepRoute> stack = new Stack<>();
        // 将开始节点压入栈中
        stack.add(new StepRoute(startStepUuid));
        // 当栈不为空时，继续循环
        while(!stack.isEmpty()) {
            count++;
            stackSize = Math.max(stack.size(), stackSize);
            // 弹出栈顶元素，即当前节点
            StepRoute current = stack.pop();
            // 当前节点的前置节点UUID列表
            List<String> preStepUuidList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(allProcessConvergeList)) {
                for (Map<String, String> map : allProcessConvergeList) {
                    if (Objects.equals(map.get("convergeUuid"), current.getStepUuid())) {
                        preStepUuidList.add(map.get("processStepUuid"));
                    }
                }
            }
            // 找出当前节点的所有下一个节点UUID列表
            List<String> toStepUuidList = new ArrayList<>();
            for (ProcessStepRelVo processStepRelVo : allProcessStepRelList) {
                if (Objects.equals(processStepRelVo.getFromStepUuid(), current.getStepUuid()) && Objects.equals(processStepRelVo.getType(), ProcessFlowDirection.FORWARD.getValue())) {
                    toStepUuidList.add(processStepRelVo.getToStepUuid());
                }
            }
            if (MapUtils.isNotEmpty(uuid2NameMap)) {
                logger.debug("当前节点为: " + uuid2NameMap.get(current.getStepUuid()));
                JSONArray toStepNameArray = new JSONArray();
                for (String toStepUuid : toStepUuidList) {
                    toStepNameArray.add(uuid2NameMap.get(toStepUuid));
                }
                logger.debug("紧邻的下一个节点列表为: " + toStepNameArray);
                logger.debug("开始遍历紧邻的下一个节点列表...");
            }
            // 将下一个节点压入栈中
            for (String toStepUuid : toStepUuidList) {
                // 如果下一个节点是结束节点，那么这整条路径上的节点都是有效节点
                if (Objects.equals(toStepUuid, endStepUuid)) {
                    List<String> pathStepUniqueKeyList = current.getPathStepUuidList();
                    for (String stepUuid : pathStepUniqueKeyList) {
                        if (!effectiveStepUuidList.contains(stepUuid)) {
                            effectiveStepUuidList.add(stepUuid);
                        }
                    }
                    if (MapUtils.isNotEmpty(uuid2NameMap)) {
                        logger.debug("节点: " + uuid2NameMap.get(toStepUuid) + " 是结束节点");
                        JSONArray stepNameArray = new JSONArray();
                        for (String stepUuid : pathStepUniqueKeyList) {
                            stepNameArray.add(uuid2NameMap.get(stepUuid));
                        }
                        logger.debug("该路径上收集到有效节点列表为: " + stepNameArray);
                    }
                    continue;
                }
                // 如果下一个节点已经确定是有效节点时，那么从开始节点到下一个节点这段路径上的节点都是有效节点，这条路径后续节点就不用遍历了
                if (effectiveStepUuidList.contains(toStepUuid)) {
                    List<String> pathStepUniqueKeyList = current.getPathStepUuidList();
                    for (String stepUuid : pathStepUniqueKeyList) {
                        if (!effectiveStepUuidList.contains(stepUuid)) {
                            effectiveStepUuidList.add(stepUuid);
                        }
                    }
                    if (MapUtils.isNotEmpty(uuid2NameMap)) {
                        logger.debug("节点: " + uuid2NameMap.get(toStepUuid) + " 是已经遍历过的有效节点，不再继续遍历它的后置节点");
                        JSONArray stepNameArray = new JSONArray();
                        for (String stepUuid : pathStepUniqueKeyList) {
                            stepNameArray.add(uuid2NameMap.get(stepUuid));
                        }
                        logger.debug("该路径上收集到有效节点列表为: " + stepNameArray);
                    }
                    continue;
                }
                if (current.checkIsPathLoop(toStepUuid)) {
                    if (MapUtils.isNotEmpty(uuid2NameMap)) {
                        logger.debug("节点: " + uuid2NameMap.get(toStepUuid) + " 是该路径上已经出现过的节点，说明该路径成回环路了，不再继续遍历该路径");
                    }
                    continue;
                }
                if (preStepUuidList.contains(toStepUuid)) {
                    if (MapUtils.isNotEmpty(uuid2NameMap)) {
                        logger.debug("在processtask_converge表中，节点: " + uuid2NameMap.get(toStepUuid) + " 被标识当前节点: " + uuid2NameMap.get(current.getStepUuid()) + " 的前置节点，说明该路径成回环路了，不再继续遍历该路径");
                    }
                    continue;
                }
                if (MapUtils.isNotEmpty(uuid2NameMap)) {
                    logger.debug("节点: " + uuid2NameMap.get(toStepUuid) + " 被压入栈中");
                }
                stack.push(new StepRoute(toStepUuid, current));
            }
        }
        if (MapUtils.isNotEmpty(uuid2NameMap)) {
            logger.debug("总共遍历节点次数为: " + count);
            logger.debug("栈最大长度为: " + stackSize);
        }
        effectiveStepUuidList.add(endStepUuid);
        return effectiveStepUuidList;
    }

    private static class StepRoute {
        // 当前节点唯一标识
        private final String stepUuid;
        // 从开始节点到当前节点这段路径上所有节点唯一标识列表
        private final List<String> pathStepUuidList;

        public StepRoute(String stepUuid) {
            this.stepUuid = stepUuid;
            this.pathStepUuidList = new ArrayList<>();
            this.pathStepUuidList.add(this.stepUuid);
        }

        public StepRoute(String stepUuid, StepRoute route) {
            this.stepUuid = stepUuid;
            this.pathStepUuidList = route.getPathStepUuidList();
            this.pathStepUuidList.add(this.stepUuid);
        }

        public String getStepUuid() {
            return stepUuid;
        }

        public List<String> getPathStepUuidList() {
            return new ArrayList<>(pathStepUuidList);
        }

        /**
         * 检查路径是否是成环路
         * @param stepUuid
         * @return
         */
        public boolean checkIsPathLoop(String stepUuid) {
            return pathStepUuidList.contains(stepUuid);
        }
    }
}
