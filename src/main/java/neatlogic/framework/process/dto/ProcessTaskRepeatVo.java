/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.dto;

/**
 * @author linbq
 * @since 2021/9/13 16:55
 **/
public class ProcessTaskRepeatVo {
    private Long processTaskId;
    private Long repeatGroupId;

    public ProcessTaskRepeatVo() {
    }

    public ProcessTaskRepeatVo(Long processTaskId, Long repeatGroupId) {
        this.processTaskId = processTaskId;
        this.repeatGroupId = repeatGroupId;
    }

    public Long getProcessTaskId() {
        return processTaskId;
    }

    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }

    public Long getRepeatGroupId() {
        return repeatGroupId;
    }

    public void setRepeatGroupId(Long repeatGroupId) {
        this.repeatGroupId = repeatGroupId;
    }
}
