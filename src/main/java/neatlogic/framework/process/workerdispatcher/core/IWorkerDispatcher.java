package neatlogic.framework.process.workerdispatcher.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import neatlogic.framework.process.dto.ProcessTaskStepWorkerVo;
import neatlogic.framework.process.exception.processtask.ProcessTaskException;
import org.springframework.util.ClassUtils;

import java.util.List;

public interface IWorkerDispatcher {

	/**
	 * @Author: chenqiwei
	 * @Time:Jun 30, 2019
	 * @Description: 返回中文名
	 * @param @return
	 * @return String
	 */
	public String getName();

	/**
	 * @Author: chenqiwei
	 * @Time:Aug 26, 2019
	 * @Description: 获取分派器配置
	 * @param @return
	 * @return JSONObject
	 */
	public JSONArray getConfig();

	/**
	 * @Author: chenqiwei
	 * @Time:Aug 26, 2019
	 * @Description: 获取帮助
	 * @param @return
	 * @return String
	 */
	public String getHelp();

	/**
	 * 
	 * @Author: chenqiwei
	 * @Time:Jun 30, 2019
	 * @Description: 返回处理人
	 * @param @return
	 * @return String
	 */
	public List<ProcessTaskStepWorkerVo> getWorker(ProcessTaskStepVo processTaskStepVo, JSONObject configObj) throws ProcessTaskException;
	
	/**
	 * @Author: 
	 * @Time:
	 * @Description: 返回类名
	 * @param @return
	 * @return String
	 */
	public default String getClassName() {
		return ClassUtils.getUserClass(this.getClass()).getName();
	}
}
