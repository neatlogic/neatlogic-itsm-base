package neatlogic.framework.process.dto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

public class ProcessTaskStepNotifyPolicyVo {

	private Long processTaskStepId;
	private Long policyId;
	private String policyName;
	private String policyHandler;
	private String policyConfig;
	private String policyConfigHash;
	public Long getProcessTaskStepId() {
		return processTaskStepId;
	}
	public void setProcessTaskStepId(Long processTaskStepId) {
		this.processTaskStepId = processTaskStepId;
	}
	public Long getPolicyId() {
		return policyId;
	}
	public void setPolicyId(Long policyId) {
		this.policyId = policyId;
	}
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getPolicyHandler() {
		return policyHandler;
	}

	public void setPolicyHandler(String policyHandler) {
		this.policyHandler = policyHandler;
	}

	public String getPolicyConfig() {
		return policyConfig;
	}
	public void setPolicyConfig(String policyConfig) {
		this.policyConfig = policyConfig;
	}
	public String getPolicyConfigHash() {
		if(StringUtils.isBlank(policyConfigHash) && StringUtils.isNotBlank(policyConfig)) {
			policyConfigHash = DigestUtils.md5DigestAsHex(policyConfig.getBytes());
		}
		return policyConfigHash;
	}
	public void setPolicyConfigHash(String policyConfigHash) {
		this.policyConfigHash = policyConfigHash;
	}
}
