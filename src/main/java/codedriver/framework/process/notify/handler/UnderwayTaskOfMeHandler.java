package codedriver.framework.process.notify.handler;

import codedriver.framework.common.constvalue.Expression;
import codedriver.framework.common.constvalue.FormHandlerType;
import codedriver.framework.common.constvalue.ParamType;
import codedriver.framework.common.dto.ValueTextVo;
import codedriver.framework.condition.core.ConditionHandlerFactory;
import codedriver.framework.condition.core.IConditionHandler;
import codedriver.framework.dto.ConditionParamVo;
import codedriver.framework.notify.core.NotifyContentHandlerBase;
import codedriver.framework.notify.handler.EmailNotifyHandler;
import codedriver.framework.process.column.core.IProcessTaskColumn;
import codedriver.framework.process.column.core.ProcessTaskColumnFactory;
import codedriver.framework.process.constvalue.ProcessConditionModel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 待我处理的工单
 */
@Component
public class UnderwayTaskOfMeHandler extends NotifyContentHandlerBase {

	public enum ConditionOptions{
		STEPTEAM("stepteam","处理组",Expression.INCLUDE.getExpression());

		private String value;
		private String text;
		private String expression;
		private ConditionOptions(String value, String text, String expression) {
			this.value = value;
			this.text = text;
			this.expression = expression;
		}
		public String getValue() {
			return value;
		}
		public String getText() {
			return text;
		}

		public static ConditionOptions getConditionOption(String _value) {
			for(ConditionOptions e : values()) {
				if(e.value.equals(_value)) {
					return e;
				}
			}
			return null;
		}
	}

	private static Map<String, JSONArray> messageAttrMap = new HashMap<>();

	/**
	 * 不同的通知内容与通知方式插件对应不同的属性
	 * 例如此插件下的邮件通知有邮件标题、邮件正文、接收人
	 * 将来扩展通知方式插件时，可在messageAttrMap中put对应的插件类与属性
	 */
	static {
		messageAttrMap.put(EmailNotifyHandler.class.getName(),new JSONArray(){
			{
				this.add(new JSONObject(){
					{
						this.put("text","标题");
						this.put("value","title");
						this.put("isEditable",1);
						this.put("controller", FormHandlerType.INPUT.toString());
					}
				});
				this.add(new JSONObject(){
					{
						this.put("text","内容");
						this.put("value","content");
						this.put("isEditable",1);
						this.put("controller", FormHandlerType.TEXTAREA.toString());
					}
				});
				this.add(new JSONObject(){
					{
						this.put("text","接收人");
						this.put("value","to");
						this.put("controller", FormHandlerType.USERSELECT.toString());
						this.put("defaultValue","工单内容对应的处理人");
						this.put("isEditable",0);
					}
				});
			}
		});
	}

	@Override
	public String getName() {
		return "待我处理的工单";
	}

	@Override
	public String getType() {
		return Type.DYNAMIC.getValue();
	}

	/**
	 * 获取工单筛选条件
	 * @return
	 */
	@Override
	protected List<ConditionParamVo> getMyConditionOptionList() {
		List<ConditionParamVo> notifyPolicyParamList = new ArrayList<>();
		String conditionModel = ProcessConditionModel.SIMPLE.getValue();
		for(ConditionOptions option : ConditionOptions.values()) {
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
				}

				param.setIsEditable(0);
				notifyPolicyParamList.add(param);
			}
		}
		return notifyPolicyParamList;
	}

	/**
	 * 根据通知方式插件获取发件相关属性
	 * @param handler
	 * @return
	 */
	@Override
	protected JSONArray getMyMessageAttrList(String handler) {
		return messageAttrMap.get(handler);
	}

	/**
	 * 获取工单列表数据列
	 * @return
	 */
	@Override
	protected List<ValueTextVo> getMyDataColumnList() {
		List<ValueTextVo> result = new ArrayList<>();
		Map<String, IProcessTaskColumn> columnComponentMap = ProcessTaskColumnFactory.columnComponentMap;
		Collection<IProcessTaskColumn> values = columnComponentMap.values();
		values.stream().forEach(o -> {
			if(!o.getDisabled() && o.getIsShow()){
				result.add(new ValueTextVo(o.getName(),o.getDisplayName()));
			}
		});
		return result;
	}
}
