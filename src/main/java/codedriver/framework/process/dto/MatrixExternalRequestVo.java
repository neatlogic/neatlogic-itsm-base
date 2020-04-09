package codedriver.framework.process.dto;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-04-03 14:44
 **/
public class MatrixExternalRequestVo {
    private String id;
    private String name;
    private String Config;

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
}
