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

import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.matrix.dto.MatrixFilterVo;

import java.util.List;

public class PrioritySearchVo extends BasePageVo {

    private String uuid;

    private String name;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    List<MatrixFilterVo> filterList;

    public List<MatrixFilterVo> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<MatrixFilterVo> filterList) {
        this.filterList = filterList;
    }
}
