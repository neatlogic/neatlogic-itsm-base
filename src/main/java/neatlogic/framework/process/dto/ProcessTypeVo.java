package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;

public class ProcessTypeVo {

	@EntityField(name = "nfpd.processtypevo.entityfield.id.name", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "nfpd.processtypevo.entityfield.name.name", type = ApiParamType.STRING)
	private String name;
	
	public Long getId() {
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
}
