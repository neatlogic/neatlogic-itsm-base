package codedriver.framework.process.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.common.dto.BaseEditorVo;
import codedriver.framework.restful.annotation.EntityField;
import codedriver.framework.util.SnowflakeUtil;
import com.alibaba.fastjson.annotation.JSONField;
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
	@EntityField(name = "内容", type = ApiParamType.STRING)
	private String content;
	@EntityField(name = "类型(system:系统模版;custom:自定义模版)", type = ApiParamType.STRING)
	private String type;
	@EntityField(name = "授权对象", type = ApiParamType.STRING)
	private List<String> authList;
	@JSONField(serialize = false)
	private List<ProcessCommentTemplateAuthVo> authVoList;

	public ProcessCommentTemplateVo() {}

	public Long getId() {
		if(id == null){
			id = SnowflakeUtil.uniqueLong();
		}
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		if(CollectionUtils.isEmpty(authList) && CollectionUtils.isNotEmpty(authVoList)){
			authList = new ArrayList<>();
			for(ProcessCommentTemplateAuthVo authorityVo : authVoList) {
				GroupSearch groupSearch = GroupSearch.getGroupSearch(authorityVo.getType());
				if(groupSearch != null) {
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
}
