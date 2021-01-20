package codedriver.framework.process.column.core;


import codedriver.framework.process.workcenter.table.ISqlTable;
import com.alibaba.fastjson.JSONObject;
import com.techsure.multiattrsearch.MultiAttrsObject;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

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
	
	@Override
	public Boolean getIsExport() {
	    return getMyIsExport();
	}
	
	public Boolean getMyIsExport() {
        return true;
    }

    @Override
	public Map<ISqlTable,List<String>> getSqlTableColumnMap(){
		return getMySqlTableColumnMap();
	}

	public Map<ISqlTable,List<String>> getMySqlTableColumnMap(){
		return  null;
	}
}
