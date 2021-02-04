package codedriver.framework.process.dto;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.util.SnowflakeUtil;

public class ChannelTypeRelationVo extends BasePageVo {

    private Long id;
    private String name;
    private Integer isActive;
    private int referenceCount;
    private List<ChannelVo> referenceList;
    private List<String> sourceList = new ArrayList<>();
    private List<String> targetList = new ArrayList<>();
    private List<ChannelTypeVo> sourceVoList = new ArrayList<>();
    private List<ChannelTypeVo> targetVoList = new ArrayList<>();
    
    @JSONField(serialize=false)
    private transient Boolean isAutoGenerateId = true;
    @JSONField(serialize=false)
    private transient List<Long> idList = new ArrayList<>();
    @JSONField(serialize=false)
    private transient boolean useIdList;
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
    public List<ChannelTypeVo> getSourceVoList() {
        return sourceVoList;
    }
    public void setSourceVoList(List<ChannelTypeVo> sourceVoList) {
        this.sourceVoList = sourceVoList;
    }
    public List<ChannelTypeVo> getTargetVoList() {
        return targetVoList;
    }
    public void setTargetVoList(List<ChannelTypeVo> targetVoList) {
        this.targetVoList = targetVoList;
    }
    public List<Long> getIdList() {
        return idList;
    }
    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }
    public boolean isUseIdList() {
        return useIdList;
    }
    public void setUseIdList(boolean useIdList) {
        this.useIdList = useIdList;
    }

    public List<ChannelVo> getReferenceList() {
        return referenceList;
    }

    public void setReferenceList(List<ChannelVo> referenceList) {
        this.referenceList = referenceList;
    }
}
