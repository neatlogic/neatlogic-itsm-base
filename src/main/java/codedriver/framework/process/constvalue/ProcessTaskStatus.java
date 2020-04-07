package codedriver.framework.process.constvalue;

import java.util.ArrayList;
import java.util.List;

import codedriver.framework.common.dto.ValueTextVo;

public enum ProcessTaskStatus {
	RUNNING("running", "处理中","#2d84fb"),
	ABORTED("aborted", "已终止","#f71010"),
	SUCCEED("succeed", "已成功","#25b865"),
	PENDING("pending", "待处理","#f71010"),
	FAILED("failed", "已失败","#f71010"),
	ABORTING("aborting", "终止中","#f71010"),
	BACK("back", "已回退","#f71010"),
	HANG("hang", "已挂起","#f71010"),
	DRAFT("draft", "未提交","#f71010");
	
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
