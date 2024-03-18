/*Copyright (C) $today.year  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.framework.process.notify.core;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.dao.mapper.NotifyConfigMapper;
import neatlogic.framework.dto.MailServerVo;
import neatlogic.framework.dto.UrlInfoVo;
import neatlogic.framework.notify.core.INotifyParamHandler;
import neatlogic.framework.notify.core.INotifyTriggerType;
import neatlogic.framework.notify.core.NotifyHandlerType;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import neatlogic.framework.util.HtmlUtil;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author linbq
 * @since 2021/10/15 16:55
 **/
public abstract class ProcessTaskNotifyParamHandlerBase implements INotifyParamHandler {

    protected static NotifyConfigMapper notifyConfigMapper;

    @Resource
    public void setNotifyConfigMapper(NotifyConfigMapper _notifyConfigMapper) {
        notifyConfigMapper = _notifyConfigMapper;
    }

    @Override
    public Object getText(Object object, INotifyTriggerType notifyTriggerType) {
        if (object instanceof ProcessTaskStepVo) {
            return getMyText((ProcessTaskStepVo) object, notifyTriggerType);
        }
        return null;
    }

    public abstract Object getMyText(ProcessTaskStepVo processTaskStepVo, INotifyTriggerType notifyTriggerType);

    protected String processContent(String content) {
        if (StringUtils.isNotBlank(content)) {
            content = content.replace("<p>", "");
            content = content.replace("</p>", "");
            content = content.replace("<br>", "");
            List<UrlInfoVo> urlInfoVoList = HtmlUtil.getUrlInfoList(content, "<img src=\"", "\"");
            String homeUrl = "";
            String config = notifyConfigMapper.getConfigByType(NotifyHandlerType.EMAIL.getValue());
            if (StringUtils.isNotBlank(config)) {
                MailServerVo mailServerVo = JSONObject.parseObject(config, MailServerVo.class);
                if (mailServerVo != null) {
                    homeUrl = mailServerVo.getHomeUrl();
                    if (StringUtils.isBlank(homeUrl)) {
                        homeUrl = "";
                    }
                }
            }
            content = HtmlUtil.urlReplace(content, urlInfoVoList, homeUrl);
        }
        return content;
    }
}
