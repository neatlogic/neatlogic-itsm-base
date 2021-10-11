/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto.agent;

/**
 * @author linbq
 * @since 2021/10/9 18:33
 **/
public class ProcessTaskAgentTargetVo {
    private Long processTaskAgentId;
    private String target;
    private String type;

    public Long getProcessTaskAgentId() {
        return processTaskAgentId;
    }

    public void setProcessTaskAgentId(Long processTaskAgentId) {
        this.processTaskAgentId = processTaskAgentId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
