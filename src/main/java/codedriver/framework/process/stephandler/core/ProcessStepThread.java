package codedriver.framework.process.stephandler.core;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codedriver.framework.asynchronization.thread.CodeDriverThread;
import codedriver.framework.process.dto.ProcessTaskStepVo;

public abstract class ProcessStepThread extends CodeDriverThread {
	Logger logger = LoggerFactory.getLogger(ProcessStepThread.class);
	private ProcessTaskStepVo processTaskStepVo;
	private Supplier<Integer> supplier;
	public ProcessTaskStepVo getProcessTaskStepVo() {
		return processTaskStepVo;
	}

	public void setSupplier(Supplier<Integer> supplier) {
        this.supplier = supplier;
    }

    public ProcessStepThread(ProcessTaskStepVo _processTaskStepVo) {
		this.processTaskStepVo = _processTaskStepVo;
		if (_processTaskStepVo != null) {
			this.setThreadName("PROCESSTASK-STEP-HANDLER-" + _processTaskStepVo.getId());
		}
	}
	@Override
	public final void execute() {
	    try {
	        myExecute();
	    }finally {
	        if(supplier != null) {
	            supplier.get();	            
	        }
	    }
	}
	
	protected abstract void myExecute();
}
