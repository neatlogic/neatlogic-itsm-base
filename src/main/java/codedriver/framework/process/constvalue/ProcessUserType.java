package codedriver.framework.process.constvalue;

import codedriver.framework.common.constvalue.IUserType;
import codedriver.framework.dto.UserTypeVo;

import java.util.HashMap;
import java.util.Map;

public enum ProcessUserType implements IUserType {
	MAJOR("major", "处理人",true), 
	MINOR("minor", "协助处理人",true),
	AGENT("agent","代办人",true),
	OWNER("owner","上报人",true),
	REPORTER("reporter","代报人",true),
	WORKER("worker", "待处理人",true),
	DEFAULT_WORKER("defaultworker", "异常处理人",true),
	FOCUS_USER("focususer","关注人",false)
	;

	private String status;
	private String text;
	private boolean isShow;

	private ProcessUserType(String _status, String _text,boolean _isShow) {
		this.status = _status;
		this.text = _text;
		this.isShow = _isShow;
	}

	public String getValue() {
		return status;
	}

	public String getText() {
		return text;
	}

	public boolean getIsShow() {
		return isShow;
	}

	public static String getValue(String _status) {
		for (ProcessUserType s : ProcessUserType.values()) {
			if (s.getValue().equals(_status)) {
				return s.getValue();
			}
		}
		return null;
	}

	public static String getText(String _status) {
		for (ProcessUserType s : ProcessUserType.values()) {
			if (s.getValue().equals(_status)) {
				return s.getText();
			}
		}
		return "";
	}


	@Override
	public UserTypeVo getUserType() {
		UserTypeVo vo = new UserTypeVo();
		vo.setModuleId(getModuleId());
		Map<String,String> map = new HashMap<>();
		for(ProcessUserType type : ProcessUserType.values()){
			map.put(type.getValue(),type.getText());
		}
		vo.setValues(map);
		return vo;
	}

	@Override
	public String getModuleId() {
		return "process";
	}
}
