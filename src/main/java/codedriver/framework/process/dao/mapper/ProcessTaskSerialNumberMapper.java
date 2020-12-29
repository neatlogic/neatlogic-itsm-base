package codedriver.framework.process.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import codedriver.framework.process.dto.ProcessTaskSerialNumberPolicyVo;

public interface ProcessTaskSerialNumberMapper {

    public ProcessTaskSerialNumberPolicyVo
        getProcessTaskSerialNumberPolicyLockByChannelTypeUuid(String channelTypeUuid);

    public List<ProcessTaskSerialNumberPolicyVo> getProcessTaskSerialNumberPolicyListByHandler(String handler);

    public int insertProcessTaskSerialNumberPolicy(ProcessTaskSerialNumberPolicyVo policyVo);

    public int insertProcessTaskSerialNumber(@Param("processTaskId") Long processTaskId,
        @Param("serialNumber") String serialNumber);

    public int updateProcessTaskSerialNumberPolicyByChannelTypeUuid(ProcessTaskSerialNumberPolicyVo policyVo);

    public int updateProcessTaskSerialNumberPolicySerialNumberSeedByChannelTypeUuid(
        @Param("channelTypeUuid") String channelTypeUuid, @Param("serialNumberSeed") Long serialNumberSeed);

    public int updateProcessTaskSerialNumberPolicyStartTimeByChannelTypeUuid(String channelTypeUuid);

    public int updateProcessTaskSerialNumberPolicyEndTimeByChannelTypeUuid(String channelTypeUuid);

    public int deleteProcessTaskSerialNumberPolicyByChannelTypeUuid(String channelTypeUuid);

    public int deleteProcessTaskSerialNumberByProcessTaskId(Long processTaskId);
}
