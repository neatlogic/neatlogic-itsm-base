/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.autocompleterule.core;

import codedriver.framework.process.dto.ProcessTaskStepVo;
import org.springframework.util.ClassUtils;

/**
 * @author linbq
 * @since 2021/10/29 15:47
 **/
public interface IAutoCompleteRuleHandler {

    String getHandler();

    void execute(ProcessTaskStepVo currentProcessTaskStepVo);
}
