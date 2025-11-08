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

package neatlogic.framework.process.crossover;

import neatlogic.framework.crossover.ICrossoverService;
import neatlogic.framework.process.dto.CatalogVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ICatalogCrossoverMapper extends ICrossoverService {

    CatalogVo getCatalogByUuid(String uuid);

    List<CatalogVo> getCatalogByName(String name);

    List<String> getUpwardUuidListByLftRht(@Param("lft") Integer lft, @Param("rht") Integer rht);

    List<String> getCatalogUuidListByLftRht(@Param("lft") Integer lft, @Param("rht")Integer rht);
    /**
     *
     * @Time:2020年7月7日
     * @Description: 根据左右编码查出目录及所有上级目录
     * @param lft 左编码
     * @param rht 右编码
     * @return List<CatalogVo>
     */
    List<CatalogVo> getAncestorsAndSelfByLftRht(@Param("lft") Integer lft, @Param("rht") Integer rht);

}
