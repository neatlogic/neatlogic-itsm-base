package codedriver.framework.process.constvalue;

import java.util.Arrays;
import java.util.List;

public enum ProcessMatrixAttributeType {
	INPUT("input","文本框", Arrays.asList(ProcessExpression.EQUAL, ProcessExpression.LIKE),ProcessExpression.LIKE),
	SELECT("select","下拉框", null, null),
	DATE("date","日期", Arrays.asList(ProcessExpression.EQUAL, ProcessExpression.LESSTHAN,ProcessExpression.GREATERTHAN), ProcessExpression.EQUAL),
	USER("user","用户", null, null),
	TEAM("team","用户组", null, null),
	ROLE("role","角色", null, null);
	
	private String value;
	private String text;
	private List<ProcessExpression> expressionList;
	private ProcessExpression expression;
	
	private ProcessMatrixAttributeType(String value, String text, List<ProcessExpression> expressionList, ProcessExpression expression) {
		this.value = value;
		this.text = text;
		this.expressionList = expressionList;
		this.expression = expression;
	}
	public String getValue() {
		return value;
	}
	public String getText() {
		return text;
	}
	public List<ProcessExpression> getExpressionList() {
		return expressionList;
	}
	public ProcessExpression getExpression() {
		return expression;
	}
	
	public static List<ProcessExpression> getExpressionList(String _value) {
		for (ProcessMatrixAttributeType s : ProcessMatrixAttributeType.values()) {
			if (s.getValue().equals(_value)) {
				return s.getExpressionList();
			}
		}
		return null;
	}
	
	public static ProcessExpression getExpression(String _value) {
		for (ProcessMatrixAttributeType s : ProcessMatrixAttributeType.values()) {
			if (s.getValue().equals(_value)) {
				return s.getExpression();
			}
		}
		return null;
	}
}
