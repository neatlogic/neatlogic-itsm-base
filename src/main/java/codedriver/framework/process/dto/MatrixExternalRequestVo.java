package codedriver.framework.process.dto;

import java.util.List;

import codedriver.framework.common.dto.ValueTextVo;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-04-03 14:44
 **/
public class MatrixExternalRequestVo {
    private String id;
    private String name;
    private String Config;
    private List<ValueTextVo> charsetNameList;
    private List<ValueTextVo> requestMethodList;
    private List<ValueTextVo> authTypeList;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfig() {
        return Config;
    }

    public void setConfig(String config) {
        Config = config;
    }

	public List<ValueTextVo> getCharsetNameList() {
		return charsetNameList;
	}

	public void setCharsetNameList(List<ValueTextVo> charsetNameList) {
		this.charsetNameList = charsetNameList;
	}

	public List<ValueTextVo> getRequestMethodList() {
		return requestMethodList;
	}

	public void setRequestMethodList(List<ValueTextVo> requestMethodList) {
		this.requestMethodList = requestMethodList;
	}

	public List<ValueTextVo> getAuthTypeList() {
		return authTypeList;
	}

	public void setAuthTypeList(List<ValueTextVo> authTypeList) {
		this.authTypeList = authTypeList;
	}
}
