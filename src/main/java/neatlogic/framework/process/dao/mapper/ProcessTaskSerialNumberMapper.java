package neatlogic.framework.process.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import neatlogic.framework.process.dto.ProcessTaskSerialNumberPolicyVo;

public interface ProcessTaskSerialNumberMapper {

    ProcessTaskSerialNumberPolicyVo getProcessTaskSerialNumberPolicyLockByChannelTypeUuid(String channelTypeUuid);

    ProcessTaskSerialNumberPolicyVo getProcessTaskSerialNumberPolicyByChannelTypeUuid(String channelTypeUuid);

    List<ProcessTaskSerialNumberPolicyVo> getProcessTaskSerialNumberPolicyListByHandler(String handler);

    int insertProcessTaskSerialNumberPolicy(ProcessTaskSerialNumberPolicyVo policyVo);

    int insertProcessTaskSerialNumber(@Param("processTaskId") Long processTaskId, @Param("serialNumber") String serialNumber);

    int updateProcessTaskSerialNumberPolicyByChannelTypeUuid(ProcessTaskSerialNumberPolicyVo policyVo);

    int updateProcessTaskSerialNumberPolicySerialNumberSeedByChannelTypeUuid(@Param("channelTypeUuid") String channelTypeUuid, @Param("serialNumberSeed") Long serialNumberSeed);

    int updateProcessTaskSerialNumberPolicyStartTimeByChannelTypeUuid(String channelTypeUuid);

    int updateProcessTaskSerialNumberPolicyEndTimeByChannelTypeUuid(String channelTypeUuid);

    int deleteProcessTaskSerialNumberPolicyByChannelTypeUuid(String channelTypeUuid);

    int deleteProcessTaskSerialNumberByProcessTaskId(Long processTaskId);
}
