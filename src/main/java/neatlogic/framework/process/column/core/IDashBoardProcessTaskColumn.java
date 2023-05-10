/*
Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 
 */

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
