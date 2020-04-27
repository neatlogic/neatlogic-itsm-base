package codedriver.framework.process.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.process.constvalue.ProcessExpression;
import codedriver.framework.process.constvalue.ProcessMatrixAttributeType;
import codedriver.framework.restful.annotation.EntityField;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-03-27 18:03
 **/
public class ProcessMatrixAttributeVo extends BasePageVo {
    @EntityField( name = "矩阵uuid", type = ApiParamType.STRING)
    private String matrixUuid;
    @EntityField( name = "属性uuid", type = ApiParamType.STRING)
    private String uuid;
    @EntityField( name = "属性名", type = ApiParamType.STRING)
    private String name;
    @EntityField( name = "类型", type = ApiParamType.STRING)
    private String type;
    @EntityField( name = "是否必填", type = ApiParamType.INTEGER)
    private Integer isRequired;
    @EntityField( name = "排序", type = ApiParamType.INTEGER)
    private Integer sort;
	@EntityField(name = "是否能删除", type = ApiParamType.INTEGER)
	private Integer isDeletable = 1;
    @EntityField( name = "配置信息", type = ApiParamType.STRING)
    private String config;
    @EntityField( name = "表达式列表", type = ApiParamType.JSONARRAY)
    private List<ProcessExpressionVo> expressionList;
    @EntityField( name = "默认表达式", type = ApiParamType.JSONOBJECT)
    private ProcessExpressionVo defaultExpression;
    
    public String getMatrixUuid() {
        return matrixUuid;
    }

    public void setMatrixUuid(String matrixUuid) {
        this.matrixUuid = matrixUuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Integer isRequired) {
		this.isRequired = isRequired;
	}

	public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getIsDeletable() {
		return isDeletable;
	}

	public void setIsDeletable(Integer isDeletable) {
		this.isDeletable = isDeletable;
	}

	public List<ProcessExpressionVo> getExpressionList() {
		if(expressionList == null && StringUtils.isNotBlank(type)) {
			List<ProcessExpression> expressionEnumList = ProcessMatrixAttributeType.getExpressionList(type);
			if(CollectionUtils.isNotEmpty(expressionEnumList)) {
				expressionList = new ArrayList<>();
				for(ProcessExpression expression : expressionEnumList) {
					expressionList.add(new ProcessExpressionVo(expression));
				}
			}
		}
		return expressionList;
	}

	public void setExpressionList(List<ProcessExpressionVo> expressionList) {
		this.expressionList = expressionList;
	}

	public ProcessExpressionVo getDefaultExpression() {
		if(defaultExpression == null && StringUtils.isNotBlank(type)) {
			ProcessExpression expressionEnum = ProcessMatrixAttributeType.getExpression(type);
			if(expressionEnum != null) {
				defaultExpression = new ProcessExpressionVo(expressionEnum);
			}
		}
		return defaultExpression;
	}

	public void setDefaultExpression(ProcessExpressionVo defaultExpression) {
		this.defaultExpression = defaultExpression;
	}
}
