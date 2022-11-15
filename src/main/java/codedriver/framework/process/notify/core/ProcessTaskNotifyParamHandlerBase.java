/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.notify.core;

import codedriver.framework.notify.core.INotifyParamHandler;
import codedriver.framework.process.dto.ProcessTaskStepVo;

/**
 * @author linbq
 * @since 2021/10/15 16:55
 **/
public abstract class ProcessTaskNotifyParamHandlerBase implements INotifyParamHandler {

    @Override
    public Object getText(Object object) {
        if (object instanceof ProcessTaskStepVo) {
            return getMyText((ProcessTaskStepVo) object);
        }
        return null;
    }

    public abstract Object getMyText(ProcessTaskStepVo processTaskStepVo);
}
