package codedriver.framework.process.notify.dto;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import codedriver.framework.common.dto.BasePageVo;

public class NotifyTemplateVo extends BasePageVo {

	private String uuid;
	private String name;
	private String title;
	private String content;
	private String type;
	private int isReadOnly = 0;
	private String notifyHandlerType;
	private String trigger;
	
	private transient String fcu;
	private transient String lcu;
	
	private transient String keyword;

	public NotifyTemplateVo() {
	}

	public NotifyTemplateVo(String uuid, String name, String type, Integer isReadOnly, String notifyHandlerType, String trigger, String title, String content) {
		this.uuid = uuid;
		this.name = name;
		this.title = title;
		this.content = content;
		this.type = type;
		this.isReadOnly = isReadOnly;
		this.notifyHandlerType = notifyHandlerType;
		this.trigger = trigger;
	}

	public synchronized String getUuid() {
		if (StringUtils.isBlank(uuid)) {
			uuid = UUID.randomUUID().toString().replace("-", "");
		}
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getFcu() {
		return fcu;
	}

	public void setFcu(String fcu) {
		this.fcu = fcu;
	}

	public String getLcu() {
		return lcu;
	}

	public void setLcu(String lcu) {
		this.lcu = lcu;
	}

	public int getIsReadOnly() {
		return isReadOnly;
	}

	public void setIsReadOnly(int isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public String getNotifyHandlerType() {
		return notifyHandlerType;
	}

	public void setNotifyHandlerType(String notifyHandlerType) {
		this.notifyHandlerType = notifyHandlerType;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
}
