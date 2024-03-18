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

package neatlogic.framework.process.dto.agent;

import java.util.Date;
import java.util.List;

/**
 * @author linbq
 * @since 2021/10/9 20:32
 **/
public class ProcessTaskAgentInfoVo {
    private Date beginTime;
    private Date endTime;
    private Integer isActive;
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

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public List<ProcessTaskAgentCompobVo> getCompobList() {
        return compobList;
    }

    public void setCompobList(List<ProcessTaskAgentCompobVo> compobList) {
        this.compobList = compobList;
    }
}
