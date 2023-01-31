/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.autocompleterule.core;

import neatlogic.framework.process.dto.ProcessTaskStepVo;

/**
 * @author linbq
 * @since 2021/10/29 15:47
 **/
public interface IAutoCompleteRuleHandler {

    String getHandler();

    String getName();

    /**
     *  优先级不能相同
     * @return
     */
    int getPriority();

    boolean execute(ProcessTaskStepVo currentProcessTaskStepVo);
}
