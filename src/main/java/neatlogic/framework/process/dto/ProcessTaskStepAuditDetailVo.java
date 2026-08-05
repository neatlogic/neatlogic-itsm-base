package neatlogic.framework.process.dto;

import org.apache.commons.lang3.StringUtils;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.process.audithandler.core.ProcessTaskAuditDetailTypeFactory;
import neatlogic.framework.restful.annotation.EntityField;

public class ProcessTaskStepAuditDetailVo implements Comparable<ProcessTaskStepAuditDetailVo>{
	@EntityField(name = "nfpd.processtaskstepauditdetailvo.entityfield.auditid.name", type = ApiParamType.LONG)
	private Long auditId;
	@EntityField(name = "nfpd.processtaskstepauditdetailvo.entityfield.type.name", type = ApiParamType.STRING)
	private String type;
	@EntityField(name = "nfpd.processtaskstepauditdetailvo.entityfield.typename.name", type = ApiParamType.STRING)
	private String typeName;
	@EntityField(name = "nfpd.processtaskstepauditdetailvo.entityfield.oldcontent.name", type = ApiParamType.STRING)
	private String oldContent;
	@EntityField(name = "nfpd.processtaskstepauditdetailvo.entityfield.newcontent.name", type = ApiParamType.STRING)
	private String newContent;

	public ProcessTaskStepAuditDetailVo() {

	}

	public ProcessTaskStepAuditDetailVo(Long _auditId, String _type, String _oldContent, String _newContent) {
		auditId = _auditId;
		type = _type;
		oldContent = _oldContent;
		newContent = _newContent;
	}

	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOldContent() {
		return oldContent;
	}

	public void setOldContent(String oldContent) {
		this.oldContent = oldContent;
	}

	public String getNewContent() {
		return newContent;
	}

	public void setNewContent(String newContent) {
		this.newContent = newContent;
	}

	@Override
	public int compareTo(ProcessTaskStepAuditDetailVo auditDetail) {
		return Integer.compare(ProcessTaskAuditDetailTypeFactory.getSort(type), ProcessTaskAuditDetailTypeFactory.getSort(auditDetail.getType()));
	}

	public String getTypeName() {
		if(StringUtils.isBlank(typeName) && StringUtils.isNotBlank(type)) {
			typeName = ProcessTaskAuditDetailTypeFactory.getText(type);
		}
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getChangeType() {
		if(oldContent != null && newContent != null){
			return "update";
		}else if(oldContent != null){
			return "clear";
		}else if(newContent != null){
			return "new";
		}
		return null;
	}
}
