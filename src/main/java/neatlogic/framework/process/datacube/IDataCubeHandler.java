/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.datacube;

import com.alibaba.fastjson.JSONArray;

import neatlogic.framework.process.dto.ProcessDataCubeVo;

public interface IDataCubeHandler {
    String getType();

    JSONArray getData(ProcessDataCubeVo dataCubeVo);
}
