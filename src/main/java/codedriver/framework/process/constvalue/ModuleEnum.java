package codedriver.framework.process.constvalue;

import java.util.Arrays;
import java.util.List;

import codedriver.framework.common.constvalue.IModuleEnum;

public enum ModuleEnum implements IModuleEnum{

	PROCESS("ITSM", "process", Arrays.asList("process"));

    private String text;
    private String value;
    private List<String> moduleList;

    private ModuleEnum(String _text, String _value ,List<String> _moduleList){
        this.text = _text;
        this.value = _value;
        this.moduleList = _moduleList;
    }
    
    @Override
    public String getValue(){
        return value;
    }
    
    @Override
    public String getText(){
        return text;
    }
    
    @Override
    public List<String> getModuleList(){
        return moduleList;
    }

}
