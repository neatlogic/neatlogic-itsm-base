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

package neatlogic.framework.process.stephandler.core;

import neatlogic.framework.process.constvalue.ProcessFlowDirection;
import neatlogic.framework.process.constvalue.ProcessStepMode;
import neatlogic.framework.process.constvalue.ProcessTaskStepOperationType;
import neatlogic.framework.process.dto.ProcessTaskStepRelVo;

import java.util.*;

public class ProcessTaskStepThreadComparator implements Comparator<ProcessTaskStepThread> {

    private final List<ProcessTaskStepRelVo> processTaskStepRelList;

    private final Long endProcessTaskStepId;

    public ProcessTaskStepThreadComparator(List<ProcessTaskStepRelVo> processTaskStepRelList, Long endProcessTaskStepId) {
        this.processTaskStepRelList = processTaskStepRelList;
        this.endProcessTaskStepId = endProcessTaskStepId;
    }

    /**
     * e1是新添加元素，e2是已存在的元素
     * @param e1 the first object to be compared.
     * @param e2 the second object to be compared.
     * @return 返回-1时，e1排在e2前面，否则e2排在e1前面
     */
    @Override
    public int compare(ProcessTaskStepThread e1, ProcessTaskStepThread e2) {
        if (e1.getOperationType() != e2.getOperationType()) {
            if (e1.getOperationType() != ProcessTaskStepOperationType.STEP_ACTIVE) {
                return 1;
            }
            if (e2.getOperationType() != ProcessTaskStepOperationType.STEP_ACTIVE) {
                return -1;
            }
        }
        if (Objects.equals(e1.getProcessTaskStepId(), endProcessTaskStepId)) {
            return 1;
        }
        if (Objects.equals(e2.getProcessTaskStepId(), endProcessTaskStepId)) {
            return -1;
        }
        // 如果e1是e2的后继步骤，则e2先排在e1前面
        if (checkIsSubsequentStep(e2.getProcessTaskStepId(), e1.getProcessTaskStepId())) {
            return 1;
        }
        // 如果e2是e1的后继步骤，则e1先排在e2前面
        if (checkIsSubsequentStep(e1.getProcessTaskStepId(), e2.getProcessTaskStepId())) {
            return -1;
        }
        if (e1.getMode() == ProcessStepMode.MT && e2.getMode() == ProcessStepMode.AT) {
            return -1;
        } else if (e1.getMode() == ProcessStepMode.AT && e2.getMode() == ProcessStepMode.MT) {
            return 1;
        }
        return 0;
    }

    /**
     * 判断目标步骤是不是当前步骤地后继步骤
     * @param currentProcessTaskStepId 当前步骤
     * @param targetProcessTaskStepId 目标步骤
     * @return
     */
    private boolean checkIsSubsequentStep(Long currentProcessTaskStepId, Long targetProcessTaskStepId) {
        List<List<Long>> routeList = new ArrayList<>();
        List<Long> routeStepList = new ArrayList<>();
        routeList.add(routeStepList);
        getAllRouteList(currentProcessTaskStepId, routeList, routeStepList, endProcessTaskStepId, processTaskStepRelList);
        // 如果最后一个步骤不是结束节点的路由全部删掉，因为这是回环路由
        Iterator<List<Long>> routeStepIt = routeList.iterator();
        List<Long> subsequentStepIdList = new ArrayList<>();
        while (routeStepIt.hasNext()) {
            List<Long> rsList = routeStepIt.next();
            if (!rsList.get(rsList.size() - 1).equals(endProcessTaskStepId)) {
                routeStepIt.remove();
            } else {
                for (Long id : rsList) {
                    if (!subsequentStepIdList.contains(id)) {
                        subsequentStepIdList.add(id);
                    }
                }
            }
        }
//        System.out.println(currentProcessTaskStep.getName() + " subsequentStepIdList = " + JSON.toJSONString(subsequentStepIdList));
        return subsequentStepIdList.contains(targetProcessTaskStepId);
    }

    /**
     *
     * @param processTaskStepId 当前步骤ID
     * @param routeList 收集所有后置路经列表
     * @param routeStepList 遍历过的步骤列表
     * @param endProcessTaskStepId 结束步骤ID
     * @param allProcessTaskStepRelList 所有连线列表
     */
    private void getAllRouteList(Long processTaskStepId, List<List<Long>> routeList, List<Long> routeStepList, Long endProcessTaskStepId, List<ProcessTaskStepRelVo> allProcessTaskStepRelList) {
        if (!routeStepList.contains(processTaskStepId)) {
            routeStepList.add(processTaskStepId);
            if (!processTaskStepId.equals(endProcessTaskStepId)) {
                List<Long> toProcessTaskStepIdList = new ArrayList<>();
                for (ProcessTaskStepRelVo processTaskStepRelVo : allProcessTaskStepRelList) {
                    if (Objects.equals(processTaskStepRelVo.getFromProcessTaskStepId(), processTaskStepId) && Objects.equals(processTaskStepRelVo.getType(), ProcessFlowDirection.FORWARD.getValue())) {
                        toProcessTaskStepIdList.add(processTaskStepRelVo.getToProcessTaskStepId());
                    }
                }
                List<Long> tmpRouteStepList = new ArrayList<>(routeStepList);
                for (int i = 0; i < toProcessTaskStepIdList.size(); i++) {
                    Long toProcessTaskStepId = toProcessTaskStepIdList.get(i);
                    if (i == 0) {
                        getAllRouteList(toProcessTaskStepId, routeList, routeStepList, endProcessTaskStepId, allProcessTaskStepRelList);
                    } else {
                        List<Long> newRouteStepList = new ArrayList<>(tmpRouteStepList);
                        routeList.add(newRouteStepList);
                        getAllRouteList(toProcessTaskStepId, routeList, newRouteStepList, endProcessTaskStepId, allProcessTaskStepRelList);
                    }
                }
            }
        }
    }

}
