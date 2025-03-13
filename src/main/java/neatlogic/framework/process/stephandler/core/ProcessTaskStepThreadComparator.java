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

import neatlogic.framework.process.constvalue.ProcessStepMode;
import neatlogic.framework.process.constvalue.ProcessTaskStepOperationType;
import neatlogic.framework.process.dto.ProcessTaskStepRelVo;
import neatlogic.framework.process.util.ProcessTaskUtil;

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
        List<Long> subsequentStepIdList = ProcessTaskUtil.getEffectivePostStepIdList(currentProcessTaskStepId, endProcessTaskStepId, processTaskStepRelList);
        return subsequentStepIdList.contains(targetProcessTaskStepId);
    }

}
