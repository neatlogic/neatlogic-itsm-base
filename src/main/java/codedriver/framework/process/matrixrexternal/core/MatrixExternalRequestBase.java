package codedriver.framework.process.matrixrexternal.core;

import codedriver.framework.process.constvalue.AuthType;
import codedriver.framework.process.constvalue.EncodingType;
import codedriver.framework.process.constvalue.RestfulType;
import codedriver.framework.process.exception.process.MatrixExternalException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;


import java.util.*;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-04-03 11:58
 **/
public abstract class MatrixExternalRequestBase implements IMatrixExternalRequestHandler {

    @Override
    public JSONObject getConfig(){
        JSONObject configObj = new JSONObject();
        if(CollectionUtils.isNotEmpty(this.encodingType())){
            JSONArray typeArray = new JSONArray();
            for (EncodingType type : encodingType()){
                JSONObject typeObj = new JSONObject();
                typeObj.put("text", type.getName());
                typeObj.put("value", type.getValue());
                typeArray.add(typeObj);
            }
            configObj.put("encodingArray", typeArray);
        }

        if(CollectionUtils.isNotEmpty(this.restfulType())){
            JSONArray typeArray = new JSONArray();
            for (RestfulType type : restfulType()){
                JSONObject typeObj = new JSONObject();
                typeObj.put("text", type.getName());
                typeObj.put("value", type.getValue());
                typeArray.add(typeObj);
            }
            configObj.put("requestArray", typeArray);
        }

        if(CollectionUtils.isNotEmpty(this.authType())){
            JSONArray typeArray = new JSONArray();
            for (AuthType type : authType()){
                JSONObject typeObj = new JSONObject();
                typeObj.put("text", type.getName());
                typeObj.put("value", type.getValue());
                typeArray.add(typeObj);
            }
            configObj.put("authArray", typeArray);
        }

        return configObj;
    }

    @Override
    public Map<String, List<String>> attributeHandler(String url, String root, JSONObject config) {
        String requestMethod = config.getString("requestMethod");
        String authType = config.getString("authType");
        String charsetName = config.getString("charsetName");
        String result = myHandler(url, authType, requestMethod, charsetName, config);
        System.out.println("result:"+result);
        List<String> attributeList = new ArrayList<>();
        List<String> pageAttributeList = new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();
        if (StringUtils.isNotBlank(result)){
            JSONObject dataObj = JSONObject.parseObject(result);
            String[] rootArray = root.split("\\.");
            if (rootArray.length > 1) {
                for (int i = 0; i < rootArray.length - 1; i++) {
                    if (dataObj.containsKey(rootArray[i])) {
                        dataObj = dataObj.getJSONObject(rootArray[i]);
                    }else {
                        throw new MatrixExternalException("根属性匹配错误");
                    }
                }
                root = rootArray[rootArray.length - 1];
            }
            for (Map.Entry<String, Object> entry: dataObj.entrySet()){
                if (!entry.getKey().equals(root)){
                    pageAttributeList.add(entry.getKey());
                }
            }
           JSONArray dataArray = dataObj.getJSONArray(root);
            if (CollectionUtils.isNotEmpty(dataArray)){
                JSONObject data = dataArray.getJSONObject(0);
                for (Map.Entry<String, Object> entry : data.entrySet()){
                    attributeList.add(entry.getKey());
                }
            }
        }
        map.put("attributeList", attributeList);
        map.put("pageAttributeList", pageAttributeList);
        return map;
    }

    @Override
    public JSONArray dataHandler(String url, String root, JSONObject config) {
        String requestMethod = config.getString("requestMethod");
        String authType = config.getString("authType");
        String charsetName = config.getString("charsetName");
        String result = myHandler(url, authType, requestMethod, charsetName, config);
        if (StringUtils.isNotBlank(result)) {
            JSONObject dataObj = JSONObject.parseObject(result);
            String[] rootArray = root.split("\\.");
            if (rootArray.length > 1){
                for (int i = 0; i < rootArray.length - 1; i++) {
                    if (dataObj.containsKey(rootArray[i])) {
                        dataObj = dataObj.getJSONObject(rootArray[i]);
                    }else {
                        throw new MatrixExternalException("根属性匹配错误");
                    }
                }
                root = rootArray[rootArray.length - 1];
            }

            return dataObj.getJSONArray(root);
        }
        return null;
    }

    public  abstract String myHandler(String url, String authType, String restfulType, String encodingType, JSONObject config);
}
