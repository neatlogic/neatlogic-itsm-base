package codedriver.framework.process.constvalue;

import java.util.ArrayList;
import java.util.List;

import codedriver.framework.common.dto.ValueTextVo;

public enum ProcessTaskStatus {
	RUNNING("running", "处理中","#2d84fb"),
	ABORTED("aborted", "已终止","#2d84fb"),
	SUCCEED("succeed", "已成功","#2d84fb"),
	PENDING("pending", "待处理","#2d84fb"),
	FAILED("failed", "已失败","#2d84fb"),
	ABORTING("aborting", "终止中","#2d84fb"),
	BACK("back", "已回退","#2d84fb"),
	HANG("hang", "已挂起","#2d84fb"),
	DRAFT("draft", "未提交","#2d84fb");
	
	private String status;
	private String text;
	private String color;

	private static List<ValueTextVo> processTaskStatusList;
	private ProcessTaskStatus(String _status, String _text,String _color) {
		this.status = _status;
		this.text = _text;
		this.color = _color;
	}

	public String getValue() {
		return status;
	}

	public String getText() {
		return text;
	}

	public String getColor() {
		return color;
	}

	public static String getValue(String _status) {
		for (ProcessTaskStatus s : ProcessTaskStatus.values()) {
			if (s.getValue().equals(_status)) {
				return s.getValue();
			}
		}
		return null;
	}

	public static String getText(String _status) {
		for (ProcessTaskStatus s : ProcessTaskStatus.values()) {
			if (s.getValue().equals(_status)) {
				return s.getText();
			}
		}
		return "";
	}
	
	public static String getColor(String _status) {
		for (ProcessTaskStatus s : ProcessTaskStatus.values()) {
			if (s.getValue().equals(_status)) {
				return s.getColor();
			}
		}
		return "";
	}

	public static List<ValueTextVo> getProcessTaskStatusList(){
		if(processTaskStatusList == null) {
			processTaskStatusList = new ArrayList<>();
			for (ProcessTaskStatus s : ProcessTaskStatus.values()) {
				processTaskStatusList.add(new ValueTextVo(s.getValue(), s.getText()));
			}
		}
		return processTaskStatusList;
	}
}
