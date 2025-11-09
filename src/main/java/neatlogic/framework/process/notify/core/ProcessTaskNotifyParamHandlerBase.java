/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.process.notify.core;

import com.alibaba.fastjson.JSON;
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
            ProcessTaskStepVo processTaskStepVo = (ProcessTaskStepVo) object;
            processTaskStepVo.setIsAutoGenerateId(false);
            return getMyText(processTaskStepVo, notifyTriggerType);
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
                MailServerVo mailServerVo = JSON.parseObject(config, MailServerVo.class);
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
