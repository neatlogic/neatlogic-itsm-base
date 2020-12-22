package codedriver.framework.process.notify.handler;

import codedriver.framework.notify.core.NotifyContentHandlerBase;
import org.springframework.stereotype.Component;

@Component
public class UnderwayTaskOfMeHandler extends NotifyContentHandlerBase {

	@Override
	public String getName() {
		return "待我处理的工单";
	}

}
