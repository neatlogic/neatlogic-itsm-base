/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.dto.processconfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linbq
 * @since 2021/5/20 18:00
 **/
public class ActionConfigVo {
    private String handler;
    private List<ActionConfigActionVo> actionList = new ArrayList<>();

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public List<ActionConfigActionVo> getActionList() {
        return actionList;
    }

    public void setActionList(List<ActionConfigActionVo> actionList) {
        this.actionList = actionList;
    }
}
