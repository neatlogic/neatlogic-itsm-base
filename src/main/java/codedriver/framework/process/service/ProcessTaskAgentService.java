/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.service;

import java.util.List;

/**
 * @author linbq
 * @since 2021/10/11 15:50
 **/
public interface ProcessTaskAgentService {

    List<String> getFromUserUuidListByToUserUuidAndChannelUuid(String toUserUuid, String channelUuid);

    List<String> getChannelUuidListByProcessTaskAgentId(Long processTaskAgentId);
}
