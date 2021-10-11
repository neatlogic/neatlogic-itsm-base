/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto.agent;

import java.util.Date;
import java.util.List;

/**
 * @author linbq
 * @since 2021/10/9 20:32
 **/
public class ProcessTaskAgentInfoVo {
    private Date beginTime;
    private Date endTime;
    private List<ProcessTaskAgentCompobVo> compobList;

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

    public List<ProcessTaskAgentCompobVo> getCompobList() {
        return compobList;
    }

    public void setCompobList(List<ProcessTaskAgentCompobVo> compobList) {
        this.compobList = compobList;
    }
}
