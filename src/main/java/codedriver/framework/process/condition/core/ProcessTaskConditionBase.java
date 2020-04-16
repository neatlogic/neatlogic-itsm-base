package codedriver.framework.process.condition.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.process.constvalue.ProcessExpression;
import codedriver.framework.process.constvalue.ProcessWorkcenterField;
import codedriver.framework.process.dto.condition.ConditionVo;
import codedriver.framework.util.TimeUtil;

public abstract class ProcessTaskConditionBase implements IProcessTaskCondition {

	@Override
	public String getEsWhere(ConditionVo condition,List<ConditionVo> conditionList) {
		String where = getMyEsWhere(condition,conditionList);
		if(StringUtils.isEmpty(where)) {
			Object value = condition.getValueList().get(0);
			if(condition.getValueList().size()>1) {
				value = String.join("','",condition.getValueList());
			}
			where = String.format(ProcessExpression.getExpressionEs(condition.getExpression()),ProcessWorkcenterField.getConditionValue(condition.getName()),String.format("'%s'",  value));
		}
		return where;
	}
	
	protected String getMyEsWhere(ConditionVo condition,List<ConditionVo> conditionList) {
		return null;
	}

	
	protected String getDateEsWhere(ConditionVo condition,List<ConditionVo> conditionList) {
		JSONArray dateJSONArray = JSONArray.parseArray(JSON.toJSONString(condition.getValueList()));
		String where = StringUtils.EMPTY;
		if(CollectionUtils.isNotEmpty(dateJSONArray)) {
			JSONObject dateValue = JSONObject.parseObject(dateJSONArray.get(0).toString());
			SimpleDateFormat format = new SimpleDateFormat(TimeUtil.TIME_FORMAT);
			String startTime = StringUtils.EMPTY;
			String endTime = StringUtils.EMPTY;
			String expression = condition.getExpression();
			if(dateValue.containsKey(ProcessWorkcenterField.STARTTIME.getValue())) {
				startTime = format.format(new Date(dateValue.getLong(ProcessWorkcenterField.STARTTIME.getValue())));
				endTime = format.format(new Date(dateValue.getLong(ProcessWorkcenterField.ENDTIME.getValue())));
			}else {
				startTime = TimeUtil.timeTransfer(dateValue.getInteger("timeRange"), dateValue.getString("timeUnit"));
				endTime = TimeUtil.timeNow();
			}
			if(StringUtils.isEmpty(startTime)) {
				expression = ProcessExpression.LESSTHAN.getExpression();
				startTime = endTime;
			}else if(StringUtils.isEmpty(endTime)) {
				expression = ProcessExpression.GREATERTHAN.getExpression();
			}
			where = String.format(ProcessExpression.getExpressionEs(expression),ProcessWorkcenterField.getConditionValue(condition.getName()),startTime,endTime);
			
		}
		return where;
	}
}
