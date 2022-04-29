/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.exception.workcenter;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

public class WorkcenterNotFoundException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = -6039692333884409352L;

	public WorkcenterNotFoundException(String handler) {
		super("找不到：'" + handler + "' 工单中心分类");
	}

}
