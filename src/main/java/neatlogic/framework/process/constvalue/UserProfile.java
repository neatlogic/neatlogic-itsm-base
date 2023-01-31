package neatlogic.framework.process.constvalue;

import neatlogic.framework.common.constvalue.IUserProfile;
import neatlogic.framework.common.util.ModuleUtil;
import neatlogic.framework.dto.UserProfileVo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.List;

public enum UserProfile implements IUserProfile{
	PROCESSTASK_SUCCESS("processtasksuccess","服务上报成功",Arrays.asList(UserProfileOperate.KEEP_ON_CREATE_TASK,UserProfileOperate.VIEW_PROCESSTASK_DETAIL,UserProfileOperate.BACK_CATALOG_LIST));

	private final String value;
	private final String text;
	private final List<UserProfileOperate> userProfileOperateList;
	
	private UserProfile(String _value,String _text,List<UserProfileOperate> _userProfileOperateList){
		this.value = _value;
		this.text = _text;
		this.userProfileOperateList = _userProfileOperateList;
	}

	public String getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
	
	public JSONArray getUserProfileOperateList() {
		JSONArray userProfileOperateArray = new JSONArray();
		for(UserProfileOperate userProfileOperate : userProfileOperateList) {
			JSONObject json = new JSONObject();
			json.put("value", userProfileOperate.getValue());
			json.put("text", userProfileOperate.getText());
			userProfileOperateArray.add(json);
		}
		return userProfileOperateArray;
	}

	public static String getText(String value){
        for (UserProfile f : UserProfile.values()){
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
        	configJson.put("userProfileOperateList", f.getUserProfileOperateList());
        	userProfileArray.add(configJson);
        }
        userProfileVo.setConfig(userProfileArray.toJSONString());
        return userProfileVo;
	}
	
}
