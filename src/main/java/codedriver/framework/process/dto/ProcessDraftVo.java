package codedriver.framework.process.dto;

import java.util.Date;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.annotation.JSONField;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.EntityField;

public class ProcessDraftVo {
	
	@EntityField(name = "草稿uuid", type = ApiParamType.STRING)
	private String uuid;
	
	@EntityField(name = "流程uuid", type = ApiParamType.STRING)
	private String processUuid;

	@EntityField(name = "流程名称", type = ApiParamType.STRING)
	private String name;

	@EntityField(name = "流程图配置", type = ApiParamType.JSONOBJECT)
	private JSONObject config;
	
	@EntityField(name = "保存时间", type = ApiParamType.LONG)
	private Date fcd;
	@JSONField(serialize=false)
	private String fcu;
	@JSONField(serialize=false)
	private String md5;

	@JSONField(serialize = false)
	private String configStr;

	public synchronized String getUuid() {
		if (StringUtils.isBlank(uuid)) {
			uuid = UUID.randomUUID().toString().replace("-", "");
		}
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getProcessUuid() {
		return processUuid;
	}

	public void setProcessUuid(String processUuid) {
		this.processUuid = processUuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JSONObject getConfig() {
		return config;
	}

	public void setConfig(String configStr) {
		this.config = JSONObject.parseObject(configStr);
	}

	public Date getFcd() {
		return fcd;
	}

	public void setFcd(Date fcd) {
		this.fcd = fcd;
	}

	public String getFcu() {
		return fcu;
	}

	public void setFcu(String fcu) {
		this.fcu = fcu;
	}

	public String getMd5() {
		if(md5 != null) {
			return md5;
		}
		if (config != null) {
			return null;
		}
		md5 = "{MD5}" + DigestUtils.md5DigestAsHex(config.toJSONString().getBytes());
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getConfigStr() {
		if (config != null) {
			return config.toJSONString();
		}
		return configStr;
	}
}
