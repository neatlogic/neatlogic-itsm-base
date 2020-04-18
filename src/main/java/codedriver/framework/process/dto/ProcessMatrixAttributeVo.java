package codedriver.framework.process.dto;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.restful.annotation.EntityField;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-03-27 18:03
 **/
public class ProcessMatrixAttributeVo extends BasePageVo {
    @EntityField( name = "矩阵uuid", type = ApiParamType.STRING)
    private String matrixUuid;
    @EntityField( name = "uuid", type = ApiParamType.STRING)
    private String uuid;
    @EntityField( name = "name", type = ApiParamType.STRING)
    private String name;
    @EntityField( name = "type", type = ApiParamType.STRING)
    private String type;
    @EntityField( name = "type", type = ApiParamType.INTEGER)
    private Integer isRequired;
    @EntityField( name = "sort", type = ApiParamType.INTEGER)
    private Integer sort;
    @EntityField( name = "config", type = ApiParamType.STRING)
    private String config;

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

//    @Override
//    public boolean equals(Object obj) {
//        if(!(obj instanceof ProcessMatrixAttributeVo)) {
//            return false;
//        }
//        ProcessMatrixAttributeVo attributeVo = (ProcessMatrixAttributeVo) obj;
//        if (this == attributeVo) {
//            return true;
//        }
//        if (attributeVo.uuid.equals(this.uuid)){
//            return true;
//        }else {
//            return false;
//        }
//    }
//
//    @Override
//    public int hashCode() {
//        int result = uuid.hashCode();
//        result = 17 * result + matrixUuid.hashCode();
//        result = 17 * result + name.hashCode();
//        return result;
//    }
}
