package neatlogic.framework.process.constvalue;

public enum UserProfileOperate{
	KEEP_ON_CREATE_TASK("keeponcreatetask","继续上报"),
	VIEW_PROCESSTASK_DETAIL("viewprocesstaskdetail","查看工单详情"),
	BACK_CATALOG_LIST("backcataloglist","返回服务目录列表");
	
	private String value;
	private String text;
	
	private UserProfileOperate(String _value,String _text){
		this.value = _value;
		this.text = _text;
	}

	public String getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

}
