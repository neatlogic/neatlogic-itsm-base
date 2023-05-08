package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import java.util.List;

public class ProcessCommentTemplateVo extends BaseEditorVo {

    public enum TempalteType {
        SYSTEM("system", "系统模版"), CUSTOM("custom", "自定义模版");

        private String name;
        private String text;

        private TempalteType(String _name, String _text) {
            this.name = _name;
            this.text = _text;
        }

        public String getValue() {
            return name;
        }

        public String getText() {
            return text;
        }

        public static String getText(String name) {
            for (TempalteType s : TempalteType.values()) {
                if (s.getValue().equals(name)) {
                    return s.getText();
                }
            }
            return "";
        }
    }

    @EntityField(name = "主键ID", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "内容", type = ApiParamType.STRING)
    private String content;
    @EntityField(name = "类型(system:系统模版;custom:自定义模版)", type = ApiParamType.STRING)
    private String type;
    @EntityField(name = "授权对象", type = ApiParamType.STRING)
    private List<String> authList;
    @EntityField(name = "当前用户是否可编辑", type = ApiParamType.INTEGER)
    private Integer isEditable;

    public ProcessCommentTemplateVo() {
    }

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getAuthList() {
        return authList;
    }

    public void setAuthList(List<String> authList) {
        this.authList = authList;
    }

    public Integer getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Integer isEditable) {
        this.isEditable = isEditable;
    }
}
