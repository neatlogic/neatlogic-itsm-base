package codedriver.framework.process.notify.handler;

import java.util.ArrayList;
import java.util.List;

import codedriver.framework.notify.dto.NotifyTriggerVo;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.Expression;
import codedriver.framework.common.constvalue.ParamType;
import codedriver.framework.condition.core.ConditionHandlerFactory;
import codedriver.framework.condition.core.IConditionHandler;
import codedriver.framework.dto.ConditionParamVo;
import codedriver.framework.dto.ExpressionVo;
import codedriver.framework.notify.core.NotifyPolicyHandlerBase;
import codedriver.framework.process.constvalue.ConditionProcessTaskOptions;
import codedriver.framework.process.constvalue.ProcessConditionModel;
import codedriver.framework.process.constvalue.ProcessTaskGroupSearch;
import codedriver.framework.process.constvalue.ProcessTaskParams;
import codedriver.framework.process.notify.core.TaskStepNotifyTriggerType;
import codedriver.framework.process.notify.core.SubtaskNotifyTriggerType;
@Component
public class TaskStepNotifyPolicyHandler extends NotifyPolicyHandlerBase {

	@Override
	public String getName() {
		return "流程步骤";
	}
	
	@Override
	public List<NotifyTriggerVo> myNotifyTriggerList() {
		List<NotifyTriggerVo> returnList = new ArrayList<>();
		for (TaskStepNotifyTriggerType notifyTriggerType : TaskStepNotifyTriggerType.values()) {
			returnList.add(new NotifyTriggerVo(notifyTriggerType.getTrigger(), notifyTriggerType.getText(),notifyTriggerType.getDescription()));
		}
		for (SubtaskNotifyTriggerType notifyTriggerType : SubtaskNotifyTriggerType.values()) {
            returnList.add(new NotifyTriggerVo(notifyTriggerType.getTrigger(), notifyTriggerType.getText(),notifyTriggerType.getDescription()));
        }
		return returnList;
	}

	@Override
	protected List<ConditionParamVo> mySystemParamList() {
		List<ConditionParamVo> notifyPolicyParamList = new ArrayList<>();
		for(ProcessTaskParams processTaskParams : ProcessTaskParams.values()) {
		    ConditionParamVo param = new ConditionParamVo();
		    param.setName(processTaskParams.getValue());
            param.setLabel(processTaskParams.getText());
            param.setParamType(processTaskParams.getParamType().getName());
            param.setParamTypeName(processTaskParams.getParamType().getText());
            param.setFreemarkerTemplate(processTaskParams.getFreemarkerTemplate());
            param.setIsEditable(0);
            notifyPolicyParamList.add(param);
		}
		return notifyPolicyParamList;
	}

    @Override
    protected List<ConditionParamVo> mySystemConditionOptionList() {
        List<ConditionParamVo> notifyPolicyParamList = new ArrayList<>();
        String conditionModel = ProcessConditionModel.CUSTOM.getValue();
        for(ConditionProcessTaskOptions option : ConditionProcessTaskOptions.values()) {
            IConditionHandler condition = ConditionHandlerFactory.getHandler(option.getValue());
            if(condition != null) {
                ConditionParamVo param = new ConditionParamVo();
                param.setName(condition.getName());
                param.setLabel(condition.getDisplayName());
                param.setController(condition.getHandler(conditionModel));
                if(condition.getConfig() != null) {
                    param.setConfig(condition.getConfig().toJSONString());
                }
                param.setType(condition.getType());
                ParamType paramType = condition.getParamType();
                if(paramType != null) {
                    param.setParamType(paramType.getName());
                    param.setParamTypeName(paramType.getText());
                    param.setDefaultExpression(paramType.getDefaultExpression().getExpression());
                    for(Expression expression : paramType.getExpressionList()) {
                        param.getExpressionList().add(new ExpressionVo(expression.getExpression(), expression.getExpressionName()));
                    }
                }
                
                param.setIsEditable(0);
                notifyPolicyParamList.add(param);
            }
        }
        return notifyPolicyParamList;
    }

	@Override
	protected void myAuthorityConfig(JSONObject config) {
		List<String> groupList = JSON.parseArray(config.getJSONArray("groupList").toJSONString(), String.class);
		groupList.add(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue());
		config.put("groupList", groupList);
	}

}
