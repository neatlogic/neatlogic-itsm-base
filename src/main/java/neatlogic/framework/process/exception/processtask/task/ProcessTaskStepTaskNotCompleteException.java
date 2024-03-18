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

package neatlogic.framework.process.exception.processtask.task;

import neatlogic.framework.process.dto.ProcessTaskStepTaskVo;
import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

/**
 * @author lvzk
 * @since 2021/8/31 14:24
 **/
public class ProcessTaskStepTaskNotCompleteException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -7336000187226502999L;

    public ProcessTaskStepTaskNotCompleteException(ProcessTaskStepTaskVo stepTaskVo) {
        super("“{0}” 不满足流转策略: “{1}”", stepTaskVo.getTaskConfigName(), stepTaskVo.getTaskConfigPolicyName());
    }
}
