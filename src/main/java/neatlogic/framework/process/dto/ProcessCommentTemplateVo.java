package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import java.util.List;

import neatlogic.framework.util.$;
public class ProcessCommentTemplateVo extends BaseEditorVo {

    public enum TempalteType {
        SYSTEM("system", "nfpd.processcommenttemplatevo.text.system"), CUSTOM("custom", "nfpd.processcommenttemplatevo.text.custom");

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
            return $.t(text);
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

    @EntityField(name = "nfpd.processcommenttemplatevo.entityfield.id.name", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "nfpd.processcommenttemplatevo.entityfield.name.name", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "nfpd.processcommenttemplatevo.entityfield.content.name", type = ApiParamType.STRING)
    private String content;
    @EntityField(name = "nfpd.processcommenttemplatevo.entityfield.type.name", type = ApiParamType.STRING)
    private String type;
    @EntityField(name = "nfpd.processcommenttemplatevo.entityfield.authlist.name", type = ApiParamType.STRING)
    private List<String> authList;
    @EntityField(name = "nfpd.processcommenttemplatevo.entityfield.iseditable.name", type = ApiParamType.INTEGER)
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
