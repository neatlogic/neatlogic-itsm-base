package codedriver.framework.process.dto;

import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.util.SnowflakeUtil;

public class ProcessTagVo extends BasePageVo{

    private Long id;
    private String name;
    
    public ProcessTagVo(String name) {
        super();
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
