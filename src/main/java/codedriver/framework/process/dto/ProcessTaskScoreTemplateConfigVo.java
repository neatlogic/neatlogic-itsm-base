package codedriver.framework.process.dto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

public class ProcessTaskScoreTemplateConfigVo {
	private String hash;
	private final String config;

	public ProcessTaskScoreTemplateConfigVo(String config) {
		this.config = config;
	}

	public String getHash() {
		if (StringUtils.isBlank(hash) && StringUtils.isNotBlank(config)) {
			hash = DigestUtils.md5DigestAsHex(config.getBytes());
		}
		return hash;
	}

    public String getConfig() {
        return config;
    }

}
