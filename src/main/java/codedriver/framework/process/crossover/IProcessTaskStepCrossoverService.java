/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.crossover;

import codedriver.framework.crossover.ICrossoverService;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import com.alibaba.fastjson.JSONObject;

public interface IProcessTaskStepCrossoverService extends ICrossoverService {

    /**
     * @param processTaskStepId 步骤id
     * @return ProcessTaskStepVo
     * @Author: linbq
     * @Time:2020年8月21日
     * @Description: 获取当前步骤信息
     */
    ProcessTaskStepVo getCurrentProcessTaskStepById(Long processTaskStepId);

}
