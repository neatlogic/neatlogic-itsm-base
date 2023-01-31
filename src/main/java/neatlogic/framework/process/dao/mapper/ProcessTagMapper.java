/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
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
