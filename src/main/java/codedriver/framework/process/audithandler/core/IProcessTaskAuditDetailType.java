package codedriver.framework.process.audithandler.core;
/**
 * 
* @Time:2020年8月11日
* @ClassName: IProcessTaskAuditDetailType 
* @Description: 工单活动内容类型接口
 */
public interface IProcessTaskAuditDetailType {
	public String getValue();

	public String getText();

	public String getParamName();

	public String getOldDataParamName();

	public int getSort();
}
