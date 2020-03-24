package codedriver.framework.process.dao.mapper.notify;

import java.util.List;

import codedriver.framework.process.notify.dto.NotifyTemplateVo;

public interface NotifyMapper {

	public int searchNotifyTemplateCount(NotifyTemplateVo notifyTemplateVo);

	public List<NotifyTemplateVo> searchNotifyTemplate(NotifyTemplateVo notifyTemplateVo);

	public NotifyTemplateVo getNotifyTemplateByUuid(String uuid);

}
