package neatlogic.framework.process.constvalue.automatic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.util.I18nUtils;

public enum FailPolicy {
    HANG("hang", "common.manualprocessing"),
    KEEP_ON("keepon", "common.flowforward"),
    BACK("back", "common.steprollback"),
    CANCEL("cancel", "common.cancelworkorder"),
    ;

    private String value;
    private String name;

    private FailPolicy(String _value, String _name) {
        this.value = _value;
        this.name = _name;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return I18nUtils.getMessage(name);
    }

    public static String getValue(String _value) {
        for (FailPolicy s : FailPolicy.values()) {
            if (s.getValue().equals(_value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getText(String _value) {
        for (FailPolicy s : FailPolicy.values()) {
            if (s.getValue().equals(_value)) {
                return s.getText();
            }
        }
        return "";
    }
    
    public static JSONArray getJSONArray() {
    	JSONArray array = new JSONArray();
    	for (FailPolicy s : FailPolicy.values()) {
    		JSONObject json = new JSONObject();
    		json.put("value", s.getValue());
    		json.put("text", s.getText());
    		array.add(json);
        }
    	return array;
    }
}
