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

	@EntityField(name = "主键ID", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "内容", type = ApiParamType.STRING)
	private String content;
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
