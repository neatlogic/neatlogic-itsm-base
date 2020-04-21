package codedriver.framework.process.matrixrexternal.core;

import codedriver.framework.common.RootComponent;
import codedriver.framework.common.dto.ValueTextVo;
import codedriver.framework.process.constvalue.AuthType;
import codedriver.framework.process.constvalue.EncodingType;
import codedriver.framework.process.constvalue.RestfulType;
import codedriver.framework.process.dto.MatrixExternalRequestVo;
import codedriver.framework.process.exception.process.MatrixExternalException;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-04-03 11:39
 **/
@RootComponent
public class MatrixExternalRequestFactory implements ApplicationListener<ContextRefreshedEvent> {

    private static Map<String, IMatrixExternalRequestHandler> map = new HashMap<>();

    private static List<MatrixExternalRequestVo> requestVoList = new ArrayList<>();

    public static List<MatrixExternalRequestVo> getRequestList(){
        return requestVoList;
    }

    public static IMatrixExternalRequestHandler getHandler(String handlerId){
        if (map.containsKey(handlerId)){
            return map.get(handlerId);
        }
        throw new MatrixExternalException("插件不存在");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        Map<String, IMatrixExternalRequestHandler> myMap = context.getBeansOfType(IMatrixExternalRequestHandler.class);
        for (Map.Entry<String, IMatrixExternalRequestHandler> entry : myMap.entrySet()){
            IMatrixExternalRequestHandler handler = entry.getValue();
            if (handler.getId() != null){
                map.put(handler.getId(), handler);
                MatrixExternalRequestVo requestVo = new MatrixExternalRequestVo();
                requestVo.setId(handler.getId());
                requestVo.setName(handler.getName());
                requestVo.setConfig(handler.getConfig().toString());
                List<EncodingType> encodingTypeList = handler.encodingType();
                if(CollectionUtils.isNotEmpty(encodingTypeList)) {
                	List<ValueTextVo> charsetNameList = new ArrayList<>();
                	for(EncodingType encodingType : encodingTypeList) {
                		charsetNameList.add(new ValueTextVo(encodingType.getValue(), encodingType.getName()));
                	}
                    requestVo.setCharsetNameList(charsetNameList);
                }
                List<RestfulType> restfulTypeList = handler.restfulType();
                if(CollectionUtils.isNotEmpty(encodingTypeList)) {
                	List<ValueTextVo> requestMethodList = new ArrayList<>();
                	for(RestfulType restfulType : restfulTypeList) {
                		requestMethodList.add(new ValueTextVo(restfulType.getValue(), restfulType.getName()));
                	}
                    requestVo.setRequestMethodList(requestMethodList);
                }
                List<AuthType> authTypes = handler.authType();
                if(CollectionUtils.isNotEmpty(encodingTypeList)) {
                	List<ValueTextVo> authTypeList = new ArrayList<>();
                	for(AuthType authType : authTypes) {
                		authTypeList.add(new ValueTextVo(authType.getValue(), authType.getName()));
                	}
                    requestVo.setAuthTypeList(authTypeList);
                }
                requestVoList.add(requestVo);
            }
        }
    }
}
