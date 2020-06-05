package codedriver.framework.process.column.core;

import org.apache.commons.collections.CollectionUtils;

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

	public Object getMyValue(JSONObject json) {
		return null;
	}
	
	@Override
	public Object getValueText(MultiAttrsObject el) throws RuntimeException{
		JSONObject commonJson = (JSONObject) el.getJSON(this.getType());
		return getMyValueText(commonJson);
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
}
