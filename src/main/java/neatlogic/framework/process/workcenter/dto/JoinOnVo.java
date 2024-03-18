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
