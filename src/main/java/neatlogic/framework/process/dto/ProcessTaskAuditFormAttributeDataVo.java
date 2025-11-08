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

import neatlogic.framework.form.dto.AttributeDataVo;

public class ProcessTaskAuditFormAttributeDataVo extends AttributeDataVo {
//    private String type;
//    private Integer sort;
    private Integer modified;

//    public String getHandler() {
//        return type;
//    }
//
//    public void setHandler(String type) {
//        this.type = type;
//    }

//    public Integer getSort() {
//        return sort;
//    }
//
//    public void setSort(Integer sort) {
//        this.sort = sort;
//    }

    public Integer getModified() {
        return modified;
    }

    public void setModified(Integer modified) {
        this.modified = modified;
    }
}
