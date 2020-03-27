package codedriver.framework.process.dto;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-03-27 18:03
 **/
public class ProcessMatrixAttributeVo {
    private String matrixUuid;
    private String uuid;
    private String name;
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

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
