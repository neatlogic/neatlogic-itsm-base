package codedriver.framework.process.constvalue.automatic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public enum CallbackType {
    WAIT("wait", "等待回调"), 
    INTERVAL("interval", "定时回调")
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
        return name;
    }

    public static String getValue(String _value) {
        for (CallbackType s : CallbackType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getName(String _value) {
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
    		JSONObject json = new JSONObject();
    		json.put("value", s.getValue());
    		json.put("text", s.getText());
    		array.add(json);
        }
    	return array;
    }
}
