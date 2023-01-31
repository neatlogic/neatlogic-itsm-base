/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.dao.mapper;

import neatlogic.framework.process.dto.ProcessTaskStepDataVo;

import java.util.List;

public interface ProcessTaskStepDataMapper {
	ProcessTaskStepDataVo getProcessTaskStepData(ProcessTaskStepDataVo processTaskStepDataVo);

	List<ProcessTaskStepDataVo> searchProcessTaskStepData(ProcessTaskStepDataVo processTaskStepDataVo);

	int replaceProcessTaskStepData(ProcessTaskStepDataVo processTaskStepDataVo);

	int deleteProcessTaskStepData(ProcessTaskStepDataVo processTaskStepDataVo);

	int deleteProcessTaskStepDataById(Long id);
}
