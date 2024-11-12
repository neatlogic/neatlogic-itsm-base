/*
 * Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package neatlogic.framework.process.dto.collection;

import com.alibaba.fastjson.JSONArray;

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
