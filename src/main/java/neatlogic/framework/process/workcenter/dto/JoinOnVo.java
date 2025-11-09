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

package neatlogic.framework.process.workcenter.dto;

/**
 * @author lvzk
 * @since 2021/7/6 10:50
 **/
public class JoinOnVo {
    private String rightKey;
    private String leftKey;
    private Boolean isRightConst;//右侧key是否常量
    private String leftTableShortName;//左侧table 缩写，优先级最高。

    public JoinOnVo() {

    }

    public JoinOnVo(String leftKey, String rightKey) {
        this.rightKey = rightKey;
        this.leftKey = leftKey;
        this.isRightConst = false;
    }

    public JoinOnVo(String leftKey, String rightKey,Boolean isRightConst) {
        this.rightKey = rightKey;
        this.leftKey = leftKey;
        this.isRightConst = isRightConst;
    }

    public JoinOnVo(String leftKey, String rightKey,Boolean isRightConst,String leftTableShortName) {
        this.rightKey = rightKey;
        this.leftKey = leftKey;
        this.isRightConst = isRightConst;
        this.leftTableShortName = leftTableShortName;
    }

    public String getRightKey() {
        return rightKey;
    }

    public void setRightKey(String rightKey) {
        this.rightKey = rightKey;
    }

    public String getLeftKey() {
        return leftKey;
    }

    public void setLeftKey(String leftKey) {
        this.leftKey = leftKey;
    }

    public Boolean getRightConst() {
        return isRightConst;
    }

    public void setRightConst(Boolean rightConst) {
        isRightConst = rightConst;
    }

    public String getLeftTableShortName() {
        return leftTableShortName;
    }

    public void setLeftTableShortName(String leftTableShortName) {
        this.leftTableShortName = leftTableShortName;
    }
}
