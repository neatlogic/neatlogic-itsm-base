package neatlogic.framework.process.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import neatlogic.framework.util.I18nUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;

import neatlogic.framework.process.datacube.DataCubeFactory;
import neatlogic.framework.process.datacube.IDataCubeHandler;

public class ProcessDataCubeVo implements Serializable {
	static Logger logger = LoggerFactory.getLogger(ProcessDataCubeVo.class);

	private static final long serialVersionUID = 6320662638466626455L;

	public enum DataCubeType {
		STATIC("static", "enum.process.datacubetype.static"),
		SQL("sql", "enum.process.datacubetype.sql"),
		REST("url", "enum.process.datacubetype.rest");
		private String type;
		private String name;

		private DataCubeType(String _type, String _name) {
			type = _type;
			name = _name;
		}

		public String getValue() {
			return type;
		}

		public String getText() {
			return I18nUtils.getMessage(name);
		}

		public String getText(String _type) {
			for (DataCubeType s : DataCubeType.values()) {
				if (s.getValue().equals(_type)) {
					return s.getText();
				}
			}
			return null;
		}
	}

	private Long id;
	private String name;
	private String type;
	private String value;
	private String sql;
	private String url;
	private JSONArray dataList;
	private Map<String, String> paramMap = new HashMap<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public JSONArray getDataList() {
		if (dataList == null) {
			IDataCubeHandler handler = DataCubeFactory.getComponent(this.getType());
			if (handler != null) {
				dataList = handler.getData(this);
			}
		}
		return dataList;
	}

	public void setDataList(JSONArray _dataList) {
		this.dataList = _dataList;
	}

	public Map<String, String> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}

}
