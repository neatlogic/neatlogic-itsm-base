/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package neatlogic.framework.process.exception.workcenter;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class WorkcenterNoAuthException extends ApiRuntimeException {

	private static final long serialVersionUID = 834889107197646727L;

	public WorkcenterNoAuthException(String name) {
		super("您没有工单中心“{0}”权限，请联系管理员", name);
	}
}
