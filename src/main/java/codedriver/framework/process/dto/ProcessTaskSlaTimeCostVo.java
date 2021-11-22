/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto;

/**
 * @author linbq
 * @since 2021/11/22 18:30
 **/
public class ProcessTaskSlaTimeCostVo {
    /**
     * 直接耗时
     */
    private long realTimeCost;
    /**
     * 工作时间耗时
     */
    private long timeCost;

    public long getRealTimeCost() {
        return realTimeCost;
    }

    public void setRealTimeCost(long realTimeCost) {
        this.realTimeCost = realTimeCost;
    }

    public long getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(long timeCost) {
        this.timeCost = timeCost;
    }
}
