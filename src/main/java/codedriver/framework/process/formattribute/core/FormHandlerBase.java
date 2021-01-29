package codedriver.framework.process.formattribute.core;

import codedriver.framework.common.dto.ValueTextVo;
import codedriver.framework.restful.core.IApiComponent;
import codedriver.framework.restful.dto.ApiVo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

public abstract class FormHandlerBase implements IFormAttributeHandler {
    @Override
    public final String getType() {
        return "form";
    }

    protected String getValue(String matrixUuid, ValueTextVo mapping, String value, IApiComponent restComponent, ApiVo api) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        try {
            JSONObject paramObj = new JSONObject();
            paramObj.put("matrixUuid", matrixUuid);
            JSONArray columnList = new JSONArray();
            columnList.add((String)mapping.getValue());
            columnList.add(mapping.getText());
            paramObj.put("columnList", columnList);
            JSONObject resultObj = (JSONObject)restComponent.doService(api, paramObj);
            JSONArray columnDataList = resultObj.getJSONArray("columnDataList");
            for (int i = 0; i < columnDataList.size(); i++) {
                JSONObject firstObj = columnDataList.getJSONObject(i);
                JSONObject textObj = firstObj.getJSONObject((String)mapping.getValue());
                if (textObj.getString("compose").contains(value)) {
                    return textObj.getString("compose");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
