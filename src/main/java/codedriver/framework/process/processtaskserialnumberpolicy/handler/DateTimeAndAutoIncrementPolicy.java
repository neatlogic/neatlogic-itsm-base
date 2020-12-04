package codedriver.framework.process.processtaskserialnumberpolicy.handler;

import org.springframework.stereotype.Service;

import codedriver.framework.process.processtaskserialnumberpolicy.core.IProcessTaskSerialNumberPolicy;
@Service
public class DateTimeAndAutoIncrementPolicy implements IProcessTaskSerialNumberPolicy {

    @Override
    public String getName() {
        return "日期 + 自增序列";
    }

    @Override
    public String genarate() {
        // TODO Auto-generated method stub
        return null;
    }

}
