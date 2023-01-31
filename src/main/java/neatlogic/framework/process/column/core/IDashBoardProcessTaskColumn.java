/*
 * Copyright (c)  2022 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
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
