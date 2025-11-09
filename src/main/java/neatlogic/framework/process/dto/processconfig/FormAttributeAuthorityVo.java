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

package neatlogic.framework.process.dto.processconfig;

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
