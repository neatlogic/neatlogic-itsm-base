/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto;

import java.util.List;

/**
 * @author lvzk
 * @since 2021/9/9 15:57
 **/
public class ProcessStepTaskConfigVo {
    private String processStepUuid;
    private Long taskConfigId;
    private List<Long> idList;
    private List<String> rangeList;
    public ProcessStepTaskConfigVo(){
        
    }
    public ProcessStepTaskConfigVo(String uuid, Long id) {
        this.taskConfigId = id;
        this.processStepUuid = uuid;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public List<String> getRangeList() {
        return rangeList;
    }

    public void setRangeList(List<String> rangeList) {
        this.rangeList = rangeList;
    }

    public String getProcessStepUuid() {
        return processStepUuid;
    }

    public void setProcessStepUuid(String processStepUuid) {
        this.processStepUuid = processStepUuid;
    }

    public Long getTaskConfigId() {
        return taskConfigId;
    }

    public void setTaskConfigId(Long taskConfigId) {
        this.taskConfigId = taskConfigId;
    }
}
