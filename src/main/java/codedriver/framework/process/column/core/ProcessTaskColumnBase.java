package codedriver.framework.process.column.core;


import org.apache.commons.collections4.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.techsure.multiattrsearch.MultiAttrsObject;

public abstract class ProcessTaskColumnBase implements IProcessTaskColumn {

	@Override
	public Object getValue(MultiAttrsObject el) throws RuntimeException {
		JSONObject commonJson = (JSONObject) el.getJSON(this.getType());
		if(commonJson == null) {
			return CollectionUtils.EMPTY_COLLECTION;
		}
		return getMyValue(commonJson);
	}

	@Override
	public Object getMyValue(JSONObject json) {
		return null;
	}
	
	@Override
	public Object getValueText(MultiAttrsObject el) throws RuntimeException{
		JSONObject commonJson = (JSONObject) el.getJSON(this.getType());
		if(commonJson != null) {
			return getMyValueText(commonJson);
		}
		return null;
	}
	
	public Object getMyValueText(JSONObject json) {
		return null;
	}
	
	@Override
	public Boolean getIsShow() {
		return getMyIsShow();
	}
	
	public Boolean getMyIsShow() {
		return true;
	}

	@Override
	public Boolean getDisabled(){
		return false;
	}
	
	@Override
	public Boolean getIsSort() {
	    return false;
	}

	@Override
	public Object getSimpleValue(Object json){
		return null;
	}
}
