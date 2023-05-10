/*
Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
