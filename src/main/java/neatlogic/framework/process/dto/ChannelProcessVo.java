package neatlogic.framework.process.dto;

import neatlogic.framework.common.dto.BasePageVo;

public class ChannelProcessVo extends BasePageVo {

	private String channelUuid;
	private String processUuid;
	public String getChannelUuid() {
		return channelUuid;
	}
	public void setChannelUuid(String channelUuid) {
		this.channelUuid = channelUuid;
	}
	public String getProcessUuid() {
		return processUuid;
	}
	public void setProcessUuid(String processUuid) {
		this.processUuid = processUuid;
	}
	
}
