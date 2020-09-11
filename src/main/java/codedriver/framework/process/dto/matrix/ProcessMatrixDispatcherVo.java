package codedriver.framework.process.dto.matrix;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-03-27 15:57
 **/
public class ProcessMatrixDispatcherVo {
    private String matrixUuid;
    private String processUuid;
    private String processName;
    private String dispatcherName;

    public String getMatrixUuid() {
        return matrixUuid;
    }

    public void setMatrixUuid(String matrixUuid) {
        this.matrixUuid = matrixUuid;
    }

    public String getProcessUuid() {
        return processUuid;
    }

    public void setProcessUuid(String processUuid) {
        this.processUuid = processUuid;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getDispatcherName() {
        return dispatcherName;
    }

    public void setDispatcherName(String dispatcherName) {
        this.dispatcherName = dispatcherName;
    }
}
