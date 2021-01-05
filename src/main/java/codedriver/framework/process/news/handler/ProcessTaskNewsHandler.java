package codedriver.framework.process.news.handler;

import codedriver.framework.news.core.NewsHandlerBase;
import org.springframework.stereotype.Service;

/**
 * @Title: ProcessTaskNewsHandler
 * @Package codedriver.framework.process.news.handler
 * @Description: 工单消息处理器
 * @Author: linbq
 * @Date: 2020/12/31 14:28
 **/
@Service
public class ProcessTaskNewsHandler extends NewsHandlerBase {

    @Override
    public String getName() {
        return "工单处理";
    }

    @Override
    public String getDescription() {
        return "实时显示待处理工单信息，支持快速审批";
    }
}
