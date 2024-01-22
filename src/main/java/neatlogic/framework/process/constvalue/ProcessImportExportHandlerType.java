package neatlogic.framework.process.constvalue;

import neatlogic.framework.importexport.core.ImportExportHandlerType;

public enum ProcessImportExportHandlerType implements ImportExportHandlerType {
    PROCESS("process", "流程图"),
    SCORE_TEMPLATE("scoreTemplate", "评分模板"),
    COMMENT_TEMPLATE("commentTemplate", "回复模板"),
    SUBTASK_POLICY("subtaskPolicy", "子任务策略"),
    EOA_TEMPLATE("eoaTemplate", "EOA模板"),
    ;
    private String value;
    private String text;
    ProcessImportExportHandlerType(String value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getText() {
        return this.text;
    }
}
