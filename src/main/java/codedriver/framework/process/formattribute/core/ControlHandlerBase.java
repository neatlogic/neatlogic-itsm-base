package codedriver.framework.process.formattribute.core;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

public abstract class ControlHandlerBase implements IFormAttributeHandler {
    @Override
    public final String getType() {
        return "control";
    }

    @Override
    public List<String> indexFieldContentList(String data){
        List<String> contentList = myIndexFieldContentList(data);
        if(CollectionUtils.isEmpty(contentList)){
            contentList = Collections.singletonList(data);
        }
        return contentList;
    }

    protected List<String> myIndexFieldContentList(String data){
        return null;
    }

    @Override
    public Boolean isNeedSliceWord() {
        return true;
    }
}
