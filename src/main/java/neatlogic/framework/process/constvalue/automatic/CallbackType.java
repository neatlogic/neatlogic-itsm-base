package neatlogic.framework.process.constvalue.automatic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.util.$;

public enum CallbackType {
    NONE("none", "无需回调"),
    INTERVAL("interval", "轮询"),
    WAIT("wait", "等待回调")
    ;

    private String value;
    private String name;

    private CallbackType(String _value, String _name) {
        this.value = _value;
        this.name = _name;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return $.t(name);
    }

    public static String getValue(String _value) {
        for (CallbackType s : CallbackType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getText(String _value) {
        for (CallbackType s : CallbackType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getText();
            }
        }
        return "";
    }
    
    public static JSONArray getJSONArray() {
    	JSONArray array = new JSONArray();
    	for (CallbackType s : CallbackType.values()) {
    		if(!s.getValue().equals(CallbackType.NONE.getValue())) {
	    		JSONObject json = new JSONObject();
	    		json.put("value", s.getValue());
	    		json.put("text", s.getText());
	    		array.add(json);
    		}
        }
    	return array;
    }
}
