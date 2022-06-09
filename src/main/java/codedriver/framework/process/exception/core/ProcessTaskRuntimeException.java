/*
 * Copyright (c)  2022 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.exception.core;

import codedriver.framework.exception.core.ApiRuntimeException;

public class ProcessTaskRuntimeException extends ApiRuntimeException {
	private static final long serialVersionUID = 3270869999551703568L;

	public ProcessTaskRuntimeException() {
		super();
	}

	public ProcessTaskRuntimeException(String msg) {
		super(msg);
	}

	public ProcessTaskRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ProcessTaskRuntimeException(Throwable cause) {
		super(cause);
	}
}
