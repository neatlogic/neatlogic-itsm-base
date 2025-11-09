/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

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
