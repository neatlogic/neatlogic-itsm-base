/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.fulltextindex;

import neatlogic.framework.fulltextindex.core.IFullTextIndexType;

/**
 * @author lvzk
 * @since 2021/03/23
 */
public enum ProcessFullTextIndexType implements IFullTextIndexType {
    PROCESSTASK("processtask", "工单");
//    PROCESSTASK_FORM("processtask_form", "工单_表单");

    private final String type;
    private final String typeName;

    ProcessFullTextIndexType(String _type, String _typeName) {
        type = _type;
        typeName = _typeName;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public String getTypeName(String type) {
        for (ProcessFullTextIndexType t : values()) {
            if (t.getType().equals(type)) {
                return t.getTypeName();
            }
        }
        return "";
    }

    @Override
    public boolean isActiveGlobalSearch() {
        return true;
    }

}
