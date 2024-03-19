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

package neatlogic.framework.process.constvalue;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import neatlogic.framework.process.dto.ProcessTaskVo;

/**
 * 工单上报来源或办理渠道
 *
 * @author laiwt
 * @since 2022/7/6 14:17
 **/
public interface IProcessTaskSource {

    String getValue();

    String getText();

    default String getType(){
        return ProcessTaskSourceType.ITSM.getValue();
    }

    /**
     * 保存工单会执行的操作
     *
     * @param paramObj 上报暂存入参
     * @param processTaskVo 工单对象
     */
    default void saveDraft(JSONObject paramObj, ProcessTaskVo processTaskVo){
    }

    /**
     * 上报工单会执行的操作
     *
     * @param processTaskStepVo 工单当前步骤对象
     */
    default void startProcess(ProcessTaskStepVo processTaskStepVo){
    }

    /**
     * 保存工单会执行的操作
     *
     * @param processTaskStepVo 工单步骤对象
     */
    default void complete (ProcessTaskStepVo processTaskStepVo){

    }
}
