/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.exception.processtask.task;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class TaskConfigButtonNameRepeatException extends ApiRuntimeException {

	private static final long serialVersionUID = 4272882237404094409L;

	public TaskConfigButtonNameRepeatException(String name) {
		super("任务操作按钮名称：'" + name + "'已存在");
	}
}
