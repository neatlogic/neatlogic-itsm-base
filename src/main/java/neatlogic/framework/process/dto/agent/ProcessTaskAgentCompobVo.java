/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.dto.agent;

import java.util.List;

/**
 * @author linbq
 * @since 2021/10/9 20:20
 **/
public class ProcessTaskAgentCompobVo {
    private String toUserUuid;
    private List<ProcessTaskAgentTargetVo> targetList;

    public String getToUserUuid() {
        return toUserUuid;
    }

    public void setToUserUuid(String toUserUuid) {
        this.toUserUuid = toUserUuid;
    }

    public List<ProcessTaskAgentTargetVo> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<ProcessTaskAgentTargetVo> targetList) {
        this.targetList = targetList;
    }
}
