package codedriver.framework.process.workcenter.column.core;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.techsure.multiattrsearch.MultiAttrsObject;

public abstract class WorkcenterColumnBase implements IWorkcenterColumn {

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
}
