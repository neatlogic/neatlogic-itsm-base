package codedriver.framework.process.constvalue;

import java.util.List;

import codedriver.framework.common.constvalue.ParamType;
import codedriver.framework.common.constvalue.Expression;
import codedriver.framework.common.constvalue.FormHandlerType;

public enum ProcessFormHandler {
	
	FORMSELECT("formselect","下拉框",FormHandlerType.SELECT,ParamType.ARRAY,List.class.getSimpleName().toLowerCase()),
	FORMINPUT("forminput","文本框",FormHandlerType.INPUT,ParamType.STRING,String.class.getSimpleName().toLowerCase()),
	FORMTEXTAREA("formtextarea","文本域",FormHandlerType.TEXTAREA,ParamType.STRING,String.class.getSimpleName().toLowerCase()),
	FORMEDITOR("formeditor","富文本框",FormHandlerType.EDITOR,ParamType.STRING,String.class.getSimpleName().toLowerCase()),
	FORMRADIO("formradio","单选框",FormHandlerType.RADIO,ParamType.ARRAY,String.class.getSimpleName().toLowerCase()),
	FORMCHECKBOX("formcheckbox","复选框",FormHandlerType.CHECKBOX,ParamType.ARRAY,List.class.getSimpleName().toLowerCase()),
	FORMDATE("formdate","日期",FormHandlerType.DATE,ParamType.DATE,String.class.getSimpleName().toLowerCase()),
	FORMTIME("formtime","时间",FormHandlerType.TIME,ParamType.DATE,String.class.getSimpleName().toLowerCase()),
	FORMSTATICLIST("formstaticlist","静态列表",null,null,null),
	FORMCASCADELIST("formcascadelist","级联下拉",FormHandlerType.CASCADELIST,ParamType.STRING,String.class.getSimpleName().toLowerCase()),
	FORMDYNAMICLIST("formdynamiclist","动态列表",null,null,null),
	FORMDIVIDER("formdivider","分割线",null,null,null),
	FORMUSERSELECT("formuserselect", "用户选择器", FormHandlerType.USERSELECT, ParamType.ARRAY, String.class.getSimpleName().toLowerCase()),
	FORMLINK("formlink", "链接", null, ParamType.STRING, String.class.getSimpleName().toLowerCase());
	
	private String handler;
	private String handlerName;
	private FormHandlerType type;
	private ParamType paramType;
	private String dataType;
	
	private ProcessFormHandler(String _handler,String _handlerName,FormHandlerType _type,ParamType _paramType,String _dataType) {
		this.handler = _handler;
		this.handlerName = _handlerName;
		this.type = _type;
		this.paramType = _paramType;
		this.dataType = _dataType;
	}
	
	
	public static String getHandlerName(String _handler) {
		for (ProcessFormHandler s : ProcessFormHandler.values()) {
			if (s.getHandler().equals(_handler)) {
				return s.getHandlerName();
			}
		}
		return null;
	}
	
	public static FormHandlerType getType(String _handler,String processWorkcenterConditionType) {
		for (ProcessFormHandler s : ProcessFormHandler.values()) {
			if (s.getHandler().equals(_handler)) {
				
				return s.getType(processWorkcenterConditionType);
			}
		}
		return null;
	}
	
	public static List<Expression> getExpressionList(String _handler) {
		for (ProcessFormHandler s : ProcessFormHandler.values()) {
			if (s.getHandler().equals(_handler)) {
				return s.getExpressionList();
			}
		}
		return null;
	}

	public FormHandlerType getType(String processWorkcenterConditionType) {
		if(ProcessConditionModel.CUSTOM.getValue().equals(processWorkcenterConditionType)) {
			if(type == FormHandlerType.RADIO || type == FormHandlerType.CHECKBOX) {
				return FormHandlerType.SELECT;
			}
			if(type == FormHandlerType.TEXTAREA || type == FormHandlerType.EDITOR) {
				return FormHandlerType.INPUT;
			}
		}
		return type;
	}

	public static Expression getExpression(String _handler) {
		for (ProcessFormHandler s : ProcessFormHandler.values()) {
			if (s.getHandler().equals(_handler)) {
				return s.getExpression();
			}
		}
		return null;
	}
	
	public static String getDataType(String _handler) {
		for (ProcessFormHandler s : ProcessFormHandler.values()) {
			if (s.getHandler().equals(_handler)) {
				return s.getDataType();
			}
		}
		return null;
	}
	
	public List<Expression> getExpressionList() {
		return this.paramType.getExpressionList();
	}

	public String getHandler() {
		return handler;
	}


	public String getHandlerName() {
		return handlerName;
	}


	public String getDataType() {
		return dataType;
	}

	public Expression getExpression() {
		return this.paramType.getDefaultExpression();
	}


	public ParamType getParamType() {
		return paramType;
	}
	
	public static ParamType getParamType(String _handler) {
		for (ProcessFormHandler s : ProcessFormHandler.values()) {
			if (s.getHandler().equals(_handler)) {
				return s.getParamType();
			}
		}
		return null;
	}

}
