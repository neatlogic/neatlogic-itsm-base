package neatlogic.framework.process.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

public class ProcessTaskContentVo implements Serializable {
    private static final long serialVersionUID = 1097967001105204840L;
    private String hash;
    private String content;

    public ProcessTaskContentVo() {

    }

    public ProcessTaskContentVo(String content) {
        this.content = content;
    }

    public ProcessTaskContentVo(String hash, String content) {
        this.content = content;
        this.hash = hash;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHash() {
        if (StringUtils.isBlank(hash) && content != null) {
            hash = DigestUtils.md5DigestAsHex(content.getBytes());
        }
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}
