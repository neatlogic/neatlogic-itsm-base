/*
 * Copyright (c)  2022 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.exception.core.ApiException;

public class ProcessTaskException extends ApiException {

	private static final long serialVersionUID = 4314481891500443152L;

	public ProcessTaskException() {
		super();
	}

	public ProcessTaskException(String msg) {
		super(msg);
	}

	public ProcessTaskException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ProcessTaskException(Throwable cause) {
		super(cause);
	}
}
