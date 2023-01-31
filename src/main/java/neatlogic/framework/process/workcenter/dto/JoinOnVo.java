/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
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
