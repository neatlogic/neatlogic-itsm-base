/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto.processconfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linbq
 * @since 2021/5/19 17:55
 **/
public class FormAttributeAuthorityVo {
    private String action = "";
    private String type = "";
    private List<String> attributeUuidList = new ArrayList<>();
    private List<String> processStepUuidList = new ArrayList<>();

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getAttributeUuidList() {
        return attributeUuidList;
    }

    public void setAttributeUuidList(List<String> attributeUuidList) {
        this.attributeUuidList = attributeUuidList;
    }

    public List<String> getProcessStepUuidList() {
        return processStepUuidList;
    }

    public void setProcessStepUuidList(List<String> processStepUuidList) {
        this.processStepUuidList = processStepUuidList;
    }
}
