package neatlogic.framework.process.dto;

import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.util.SnowflakeUtil;

public class ProcessTagVo extends BasePageVo{

    private Long id;
    private String name;
    
    public ProcessTagVo() {
    }
    
    public ProcessTagVo(String name) {
        this.name = name;
    }

    public synchronized Long getId() {
        if(id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
}
