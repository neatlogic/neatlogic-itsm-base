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

package neatlogic.framework.process.exception.sla;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

/**
 * @author linbq
 * @since 2021/11/22 14:28
 **/
public class SlaCalculateHandlerNotFoundException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -6956721990886391194L;

    public SlaCalculateHandlerNotFoundException(String handler) {
        super("sla计算规则处理器：“{0}”不存在", handler);
    }
}
