/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.crossover;

import neatlogic.framework.crossover.ICrossoverService;
import com.alibaba.fastjson.JSONObject;

public interface IProcessTaskStartApiCrossoverService extends ICrossoverService {

    Object myDoService(JSONObject jsonObj) throws Exception;

}
