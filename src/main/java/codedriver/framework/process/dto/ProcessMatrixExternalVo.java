package codedriver.framework.process.dto;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-04-02 18:25
 **/
public class ProcessMatrixExternalVo {
//    private String uuid;
    private String matrixUuid;
    private String integrationUuid;

//    public String getUuid() {
//        return uuid;
//    }
//
//    public void setUuid(String uuid) {
//        this.uuid = uuid;
//    }

    public String getMatrixUuid() {
        return matrixUuid;
    }

    public void setMatrixUuid(String matrixUuid) {
        this.matrixUuid = matrixUuid;
    }

	public String getIntegrationUuid() {
		return integrationUuid;
	}

	public void setIntegrationUuid(String integrationUuid) {
		this.integrationUuid = integrationUuid;
	}
}
