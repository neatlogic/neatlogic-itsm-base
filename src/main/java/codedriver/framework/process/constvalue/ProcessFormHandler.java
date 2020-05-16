package codedriver.framework.process.constvalue;

import java.util.Arrays;
import java.util.List;

import codedriver.framework.common.constvalue.FormHandlerType;

public enum ProcessFormHandler {
	
	FORMSELECT("formselect","下拉框",FormHandlerType.SELECT,Arrays.asList(ProcessExpression.UNEQUAL,ProcessExpression.INCLUDE,ProcessExpression.EXCLUDE),ProcessExpression.INCLUDE,List.class.getSimpleName().toLowerCase()),
	FORMINPUT("forminput","文本框",FormHandlerType.INPUT,Arrays.asList(ProcessExpression.EQUAL,ProcessExpression.UNEQUAL,ProcessExpression.LIKE),ProcessExpression.LIKE,String.class.getSimpleName().toLowerCase()),
	FORMTEXTAREA("formtextarea","文本域",FormHandlerType.TEXTAREA,Arrays.asList(ProcessExpression.LIKE),ProcessExpression.LIKE,String.class.getSimpleName().toLowerCase()),
	FORMEDITOR("formeditor","富文本框",FormHandlerType.EDITOR,Arrays.asList(ProcessExpression.LIKE),ProcessExpression.LIKE,String.class.getSimpleName().toLowerCase()),
	FORMRADIO("formradio","单选框",FormHandlerType.RADIO,Arrays.asList(ProcessExpression.EQUAL,ProcessExpression.UNEQUAL),ProcessExpression.EQUAL,String.class.getSimpleName().toLowerCase()),
	FORMCHECKBOX("formcheckbox","复选框",FormHandlerType.CHECKBOX,Arrays.asList(ProcessExpression.INCLUDE,ProcessExpression.EXCLUDE),ProcessExpression.INCLUDE,List.class.getSimpleName().toLowerCase()),
	FORMDATE("formdate","日期",FormHandlerType.DATE,Arrays.asList(ProcessExpression.EQUAL,ProcessExpression.UNEQUAL,ProcessExpression.LESSTHAN,ProcessExpression.GREATERTHAN),ProcessExpression.EQUAL,String.class.getSimpleName().toLowerCase()),
	FORMTIME("formtime","时间",FormHandlerType.TIME,Arrays.asList(ProcessExpression.EQUAL,ProcessExpression.UNEQUAL,ProcessExpression.LESSTHAN,ProcessExpression.GREATERTHAN),ProcessExpression.EQUAL,String.class.getSimpleName().toLowerCase()),
	FORMSTATICLIST("formstaticList","静态列表",null,null,null,null),
	FORMCASCADELIST("formcascadeList","级联下拉",null,null,null,null),
	FORMDYNAMICLIST("formdynamicList","动态列表",null,null,null,null),
	FORMDIVIDER("formdivider","分割线",null,null,null,null)
	;
	
	private String handler;
	private String handlerName;
	private FormHandlerType type;
	private List<ProcessExpression> expressionList;
	private ProcessExpression expression;
	private String dataType;
	
	private ProcessFormHandler(String _handler,String _handlerName,FormHandlerType _type,List<ProcessExpression> _expressionList,ProcessExpression _expression,String _dataType) {
		this.handler = _handler;
		this.handlerName = _handlerName;
		this.type = _type;
		this.expressionList = _expressionList;
		this.expression = _expression;
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
	
	public static List<ProcessExpression> getExpressionList(String _handler) {
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

	public static ProcessExpression getExpression(String _handler) {
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
	
	public List<ProcessExpression> getExpressionList() {
		return expressionList;
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

	public ProcessExpression getExpression() {
		return expression;
	}
	
	

}
