/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.exception.workcenter;

import codedriver.framework.exception.core.ApiRuntimeException;

public class WorkcenterNoAuthException extends ApiRuntimeException {

	private static final long serialVersionUID = 834889107197646727L;

	public WorkcenterNoAuthException(String name) {
		super("您没有工单中心'" + name + "'权限，请联系管理员");
	}
}
