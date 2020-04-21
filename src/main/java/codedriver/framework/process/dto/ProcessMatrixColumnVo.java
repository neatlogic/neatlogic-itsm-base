package codedriver.framework.process.dto;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-04-10 18:25
 **/
public class ProcessMatrixColumnVo {
    private String column;
    private String value;
    private String expression;

    public ProcessMatrixColumnVo() {
	}

	public ProcessMatrixColumnVo(String column, String value) {
		this.column = column;
		this.value = value;
	}

	public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
}
