/*
Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package neatlogic.framework.process.dao.mapper;

import neatlogic.framework.common.dto.ValueTextVo;
import neatlogic.framework.process.dto.ProcessTagVo;

import java.util.List;

/**
 * @author linbq
 * @since 2021/10/15 14:35
 **/
public interface ProcessTagMapper {

    List<ValueTextVo> getProcessTagForSelect(ProcessTagVo processTagVo);

    List<ProcessTagVo> getProcessTagByNameList(List<String> tagNameList);

    List<ProcessTagVo> getProcessTagByIdList(List<Long> tagIdList);

    Long getProcessTagIdByName(String Name);

    int getProcessTagCount(ProcessTagVo processTagVo);

    int insertProcessTag(ProcessTagVo processTagVo);
}
