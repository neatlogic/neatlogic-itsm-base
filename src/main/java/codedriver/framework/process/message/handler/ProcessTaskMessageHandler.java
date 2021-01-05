package codedriver.framework.process.message.handler;

import codedriver.framework.message.core.MessageHandlerBase;
import org.springframework.stereotype.Service;

/**
 * @Title: ProcessTaskMessageHandler
 * @Package codedriver.framework.process.message.handler
 * @Description: 工单消息处理器
 * @Author: linbq
 * @Date: 2020/12/31 14:28
 **/
@Service
public class ProcessTaskMessageHandler extends MessageHandlerBase {

    @Override
    public String getName() {
        return "工单处理";
    }

    @Override
    public String getDescription() {
        return "实时显示待处理工单信息，支持快速审批";
    }
}
