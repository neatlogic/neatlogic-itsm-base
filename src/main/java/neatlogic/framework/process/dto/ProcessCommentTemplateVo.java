package neatlogic.framework.process.dto;

import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.auth.core.AuthActionChecker;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.process.auth.PROCESS_COMMENT_TEMPLATE_MODIFY;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
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
    @JSONField(serialize = false)
    private List<ProcessCommentTemplateAuthVo> authVoList;
    @EntityField(name = "当前用户是否可编辑", type = ApiParamType.INTEGER)
    private Integer isEditable;

    @JSONField(serialize = false)
    private Integer isHasModifyAuthority = 0;

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
        if (CollectionUtils.isEmpty(authList) && CollectionUtils.isNotEmpty(authVoList)) {
            authList = new ArrayList<>();
            for (ProcessCommentTemplateAuthVo authorityVo : authVoList) {
                GroupSearch groupSearch = GroupSearch.getGroupSearch(authorityVo.getType());
                if (groupSearch != null) {
                    authList.add(groupSearch.getValuePlugin() + authorityVo.getUuid());
                }
            }
        }
        return authList;
    }

    public void setAuthList(List<String> authList) {
        this.authList = authList;
    }

    public List<ProcessCommentTemplateAuthVo> getAuthVoList() {
        return authVoList;
    }

    public void setAuthVoList(List<ProcessCommentTemplateAuthVo> authVoList) {
        this.authVoList = authVoList;
    }

    public Integer getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Integer isEditable) {
        this.isEditable = isEditable;
    }

    public Integer getIsHasModifyAuthority() {
        if (isHasModifyAuthority == null) {
            isHasModifyAuthority = AuthActionChecker.check(PROCESS_COMMENT_TEMPLATE_MODIFY.class) ? 1 : 0;
        }
        return isHasModifyAuthority;
    }

    public void setIsHasModifyAuthority(Integer isHasModifyAuthority) {
        this.isHasModifyAuthority = isHasModifyAuthority;
    }
}
