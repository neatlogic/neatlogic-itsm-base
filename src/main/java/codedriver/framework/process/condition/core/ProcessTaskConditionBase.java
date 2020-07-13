package codedriver.framework.process.condition.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.Expression;
import codedriver.framework.dto.condition.ConditionVo;
import codedriver.framework.process.constvalue.ProcessWorkcenterField;
import codedriver.framework.util.TimeUtil;

public abstract class ProcessTaskConditionBase implements IProcessTaskCondition {

	@Override
	public String getEsWhere(List<ConditionVo> conditionList,Integer index) {
		ConditionVo condition = conditionList.get(index);
		String where = getMyEsWhere(index,conditionList);
		if(StringUtils.isBlank(where)) {
			Object value = StringUtils.EMPTY;
			if(condition.getValueList() instanceof String) {
				value = condition.getValueList();
			}else if(condition.getValueList() instanceof List) {
				List<String> values = JSON.parseArray(JSON.toJSONString(condition.getValueList()), String.class);
				value = String.join("','", values);
			}
			if(StringUtils.isNotBlank(value.toString())) {
				value = String.format("'%s'",  value);
			}
			where = String.format(Expression.getExpressionEs(condition.getExpression()),ProcessWorkcenterField.getConditionValue(condition.getName()),value);
			
		}
		return where;
	}
	
	protected String getMyEsWhere(Integer index,List<ConditionVo> conditionList) {
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
				expression = Expression.LESSTHAN.getExpression();
				startTime = endTime;
			}else if(StringUtils.isEmpty(endTime)) {
				expression = Expression.GREATERTHAN.getExpression();
			}
			where = String.format(Expression.getExpressionEs(expression),ProcessWorkcenterField.getConditionValue(condition.getName()),startTime,endTime);
			
		}
		return where;
	}
}
