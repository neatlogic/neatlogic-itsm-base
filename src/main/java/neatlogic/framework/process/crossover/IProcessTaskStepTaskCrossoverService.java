/*
 * Copyright (C) 2025  深圳极向量科技有限公司 All Rights Reserved.
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

import com.alibaba.fastjson.JSONArray;
import neatlogic.framework.crossover.ICrossoverService;
import neatlogic.framework.process.dto.ProcessTaskStepTaskVo;

public interface IProcessTaskStepTaskCrossoverService extends ICrossoverService {
    /**
     * 创建任务
     *
     * @param id
     * @param processTaskStepTaskVo
     * @param stepTaskUserVoList
     * @param source
     */
    Long saveTask(Long id, ProcessTaskStepTaskVo processTaskStepTaskVo, JSONArray stepTaskUserVoList, String source);

    /**
     * 完成任务
     *
     * @param id 任务id
     * @param content 回复内容
     * @param button 按钮
     * @param source 来源
     */
    Long completeTask(Long id, String content, String button, String source) throws Exception;
}
