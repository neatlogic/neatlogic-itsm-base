package codedriver.framework.process.constvalue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ModuleEnum;
import codedriver.framework.common.util.ModuleUtil;
import codedriver.framework.dto.UserProfileVo;
import codedriver.framework.restful.api.IUserProfile;

public enum UserProfile implements IUserProfile{
	PROCESSTASK_SUCESS("processtasksucess","服务上报成功");
	
	private String value;
	private String text;
	
	private UserProfile(String _value,String _text){
		this.value = _value;
		this.text = _text;
	}

	public String getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static String getText(String value){
        for (ModuleEnum f : ModuleEnum.values()){
            if (f.getValue().equals(value)){
                return f.getText();
            }
        }
        return "";
	}
	@Override
	public String getModuleId() {
		return "process";
	}
	
	@Override
	public UserProfileVo getUserProfile(){
		UserProfileVo userProfileVo = new UserProfileVo();
		userProfileVo.setModuleId(getModuleId());
		userProfileVo.setModuleName(ModuleUtil.getModuleById(getModuleId()).getName());
		JSONArray userProfileArray = new JSONArray();
        for (UserProfile f : UserProfile.values()){
        	JSONObject configJson = new JSONObject();
        	configJson.put("value", f.getValue());
        	configJson.put("text", f.getText());
        	userProfileArray.add(configJson);
        }
        userProfileVo.setConfig(userProfileArray.toJSONString());
        return userProfileVo;
	}
	
}
