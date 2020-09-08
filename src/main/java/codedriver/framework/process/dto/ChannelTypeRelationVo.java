package codedriver.framework.process.dto;

import java.util.ArrayList;
import java.util.List;

import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.util.SnowflakeUtil;

public class ChannelTypeRelationVo extends BasePageVo {

    private Long id;
    private String name;
    private Integer isActive;
    private int referenceCount;
    private List<String> sourceList = new ArrayList<>();
    private List<String> targetList = new ArrayList<>();
    
    private transient Boolean isAutoGenerateId = true;
    private transient String source;
    
    public synchronized Long getId() {
        if(id == null && isAutoGenerateId) {
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
    public Integer getIsActive() {
        return isActive;
    }
    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
    public int getReferenceCount() {
        return referenceCount;
    }
    public void setReferenceCount(int referenceCount) {
        this.referenceCount = referenceCount;
    }
    public List<String> getSourceList() {
        return sourceList;
    }
    public void setSourceList(List<String> sourceList) {
        this.sourceList = sourceList;
    }
    public List<String> getTargetList() {
        return targetList;
    }
    public void setTargetList(List<String> targetList) {
        this.targetList = targetList;
    }
    public Boolean getIsAutoGenerateId() {
        return isAutoGenerateId;
    }
    public void setIsAutoGenerateId(Boolean isAutoGenerateId) {
        this.isAutoGenerateId = isAutoGenerateId;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
}
