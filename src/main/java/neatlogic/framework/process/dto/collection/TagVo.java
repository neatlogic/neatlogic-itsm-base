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

package neatlogic.framework.process.dto.collection;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class TagVo {
    private String name;
    private String label;
    private Object value;
    private Long collectionId;
    private boolean needFilter;
    private boolean needAppend;
    private JSONArray valueList;

    public JSONArray getValueList() {
        if (valueList == null && value != null) {
            valueList = new JSONArray();
            if (value instanceof List) {
                valueList.addAll((JSONArray) value);
            } else {
                valueList.add(value);
            }
        }
        return valueList;
    }

    public String getValueString() {
        JSONArray jsonArray = getValueList();
        String returnValue = "";
        if (CollectionUtils.isNotEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.size(); i++) {
                String v = jsonArray.getString(i);
                if (StringUtils.isNotBlank(returnValue) && StringUtils.isNotBlank(v)) {
                    returnValue += ",";
                }
                returnValue += v;
            }
        }
        return returnValue;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValueList(JSONArray valueList) {
        this.valueList = valueList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public boolean isNeedFilter() {
        return needFilter;
    }

    public void setNeedFilter(boolean needFilter) {
        this.needFilter = needFilter;
    }

    public boolean isNeedAppend() {
        return needAppend;
    }

    public void setNeedAppend(boolean needAppend) {
        this.needAppend = needAppend;
    }
}
