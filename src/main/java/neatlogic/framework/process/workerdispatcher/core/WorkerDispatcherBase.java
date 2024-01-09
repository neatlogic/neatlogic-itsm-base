package neatlogic.framework.process.workerdispatcher.core;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import neatlogic.framework.process.dto.ProcessTaskStepWorkerVo;
import neatlogic.framework.process.exception.processtask.ProcessTaskException;

import java.util.List;

public abstract class WorkerDispatcherBase implements IWorkerDispatcher {
	public final List<ProcessTaskStepWorkerVo> getWorker(ProcessTaskStepVo processTaskStepVo, JSONObject configObj) throws ProcessTaskException {
		return myGetWorker(processTaskStepVo, configObj);
	}

	protected abstract List<ProcessTaskStepWorkerVo> myGetWorker(ProcessTaskStepVo processTaskStepVo, JSONObject configObj) throws ProcessTaskException;
}
