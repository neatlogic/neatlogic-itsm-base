package codedriver.framework.process.fulltextindex;

import codedriver.framework.fulltextindex.core.IFullTextIndexType;

/**
 * @author lvzk
 * @since 2021/03/23
 */
public enum FullTextIndexType implements IFullTextIndexType {
    PROCESSTASK("processtask", "工单"),
    PROCESSTASK_FORM("processtask_form", "工单_表单");

    private final String type;
    private final String typeName;

    FullTextIndexType(String _type, String _typeName) {
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
        for (FullTextIndexType t : values()) {
            if (t.getType().equals(type)) {
                return t.getTypeName();
            }
        }
        return "";
    }

}
