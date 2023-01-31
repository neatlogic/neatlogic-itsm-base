/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.dto.agent;

import neatlogic.framework.util.SnowflakeUtil;

import java.util.Date;
import java.util.List;

/**
 * @author linbq
 * @since 2021/10/9 18:32
 **/
public class ProcessTaskAgentVo {
    private Long id;
    private String fromUserUuid;
    private String toUserUuid;
    private Date beginTime;
    private Date endTime;
    private Integer isActive;
    private List<ProcessTaskAgentTargetVo> processTaskAgentTargetVos;

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromUserUuid() {
        return fromUserUuid;
    }

    public void setFromUserUuid(String fromUserUuid) {
        this.fromUserUuid = fromUserUuid;
    }

    public String getToUserUuid() {
        return toUserUuid;
    }

    public void setToUserUuid(String toUserUuid) {
        this.toUserUuid = toUserUuid;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public List<ProcessTaskAgentTargetVo> getProcessTaskAgentTargetVos() {
        return processTaskAgentTargetVos;
    }

    public void setProcessTaskAgentTargetVos(List<ProcessTaskAgentTargetVo> processTaskAgentTargetVos) {
        this.processTaskAgentTargetVos = processTaskAgentTargetVos;
    }
}
