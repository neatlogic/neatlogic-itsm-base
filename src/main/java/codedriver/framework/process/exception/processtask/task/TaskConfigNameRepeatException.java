/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.exception.processtask.task;

import codedriver.framework.exception.core.ApiRuntimeException;

public class TaskConfigNameRepeatException extends ApiRuntimeException {

	private static final long serialVersionUID = 4272882237404094408L;

	public TaskConfigNameRepeatException(String name) {
		super("任务名：'" + name + "'已存在");
	}
}
