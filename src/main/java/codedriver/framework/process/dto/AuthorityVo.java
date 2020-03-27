package codedriver.framework.process.dto;

public class AuthorityVo {

	private String catalogUuid;
	private String channelUuid;
	private String userId;
	private String teamUuid;
	private String roleName;
	public String getCatalogUuid() {
		return catalogUuid;
	}
	public void setCatalogUuid(String catalogUuid) {
		this.catalogUuid = catalogUuid;
	}
	public String getChannelUuid() {
		return channelUuid;
	}
	public void setChannelUuid(String channelUuid) {
		this.channelUuid = channelUuid;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTeamUuid() {
		return teamUuid;
	}
	public void setTeamUuid(String teamUuid) {
		this.teamUuid = teamUuid;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
}
