package neatlogic.framework.process.dto.approve;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

public class ProcessTaskApproveEntityConfigVo {
    private String hash;
    private String config;

    public ProcessTaskApproveEntityConfigVo() {

    }
    public ProcessTaskApproveEntityConfigVo(String config) {
        this.config = config;
    }

    public String getHash() {
        if (StringUtils.isBlank(hash) && config != null) {
            hash = DigestUtils.md5DigestAsHex(config.getBytes());
        }
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
