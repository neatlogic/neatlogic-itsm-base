/*
 * Copyright (c)  2022 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.exception.processtask;

import codedriver.framework.exception.core.ApiRuntimeException;

public class ProcessTaskPriorityNotMatchException extends ApiRuntimeException {

	private static final long serialVersionUID = -5766752850987530449L;

	public ProcessTaskPriorityNotMatchException() {
		super("工单优先级与当前服务优先级不匹配");
	}

}
