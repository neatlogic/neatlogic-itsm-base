package neatlogic.framework.process.constvalue;

import neatlogic.framework.importexport.core.ImportExportHandlerType;

import neatlogic.framework.util.$;
public enum ProcessImportExportHandlerType implements ImportExportHandlerType {
    PROCESS("process", "nfpc.processimportexporthandlertype.text.process"),
    SCORE_TEMPLATE("scoreTemplate", "nfpc.processimportexporthandlertype.text.score_template"),
    COMMENT_TEMPLATE("commentTemplate", "nfpc.processimportexporthandlertype.text.comment_template"),
    SUBTASK_POLICY("subtaskPolicy", "nfpc.processimportexporthandlertype.text.subtask_policy"),
    EOA_TEMPLATE("eoaTemplate", "nfpc.processimportexporthandlertype.text.eoa_template"),
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
        return $.t(text);
    }
}
