/*Copyright (C) $today.year  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.framework.process.dto;

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
