package neatlogic.framework.process.dto;

import java.io.Serializable;

public class ProcessTaskStepConfigVo implements Serializable {
	private static final long serialVersionUID = 1097967001105204845L;

	private String hash;
	private String config;

	public ProcessTaskStepConfigVo() {

	}

	public ProcessTaskStepConfigVo(String _hash, String _config) {
		hash = _hash;
		config = _config;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

}
