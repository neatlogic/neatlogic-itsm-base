package codedriver.framework.process.matrixrexternal.core;

import codedriver.framework.process.constvalue.AuthType;
import codedriver.framework.process.constvalue.EncodingType;
import codedriver.framework.process.constvalue.RestfulType;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.ClassUtils;

import java.util.List;
import java.util.Map;

public interface IMatrixExternalRequestHandler {
    /**
    * @Description: 展示名
    * @Param: []
    * @return: java.lang.String
    */
    public String getName();

    /**
    * @Description: 主键ID
    * @Param: []
    * @return: java.lang.String
    */
    public default String getId(){
        return ClassUtils.getUserClass(this.getClass()).getName();
    }

    /**
    * @Description: 编码方式
    * @Param: []
    * @return: java.util.List<codedriver.framework.process.constvalue.EncodingType>
    */
    public List<EncodingType> encodingType();

    /**
    * @Description: 请求方式
    * @Param: []
    * @return: java.util.List<codedriver.framework.process.constvalue.RestfulType>
    */
    public List<RestfulType> restfulType();

    /**
    * @Description:认证方式
    * @Param: []
    * @return: java.util.List<codedriver.framework.process.constvalue.AuthType>
    */
    public List<AuthType> authType();

    /**
    * @Description: 获取配置信息
    * @Param: []
    * @return: org.codehaus.jettison.json.JSONObject
    */
    public JSONObject getConfig();
    
    /**
    * @Description: 插件执行访问请求获取属性
    * @Param: [url, root, config]
    * @return: java.util.Map<java.lang.String,java.util.List<java.lang.String>>
    */
    public Map<String, List<String>> attributeHandler(String url, String root, JSONObject config);

    /**
    * @Description:  插件执行访问请求获取数据
    * @Param: [url, root, jsonObject]
    * @return: java.lang.String
    */
    public JSONArray dataHandler(String url, String root, JSONObject jsonObject);
}
