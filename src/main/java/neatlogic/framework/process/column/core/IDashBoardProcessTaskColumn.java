/*Copyright (C) 2023  深圳极向量科技有限公司 All Rights Reserved.

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

package neatlogic.framework.process.column.core;

import java.util.List;
import java.util.Map;

public interface IDashBoardProcessTaskColumn {
    /**
     * 根据Name获取group的Text，目前用于数值图补充count为0的选项数据
     * @param groupNameList 分组选项名列表
     * @return 返回分组选项和名 map
     */
    Map<String,String> getGroupNameTextMap(List<String> groupNameList);
}
