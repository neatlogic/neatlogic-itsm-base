package codedriver.framework.process.processtaskserialnumberpolicy.core;

import org.springframework.util.ClassUtils;

public interface IProcessTaskSerialNumberPolicy {

    public String getName();
    public String genarate();
    public default String getHandler() {
        return ClassUtils.getUserClass(this.getClass()).getName();
    }
}
