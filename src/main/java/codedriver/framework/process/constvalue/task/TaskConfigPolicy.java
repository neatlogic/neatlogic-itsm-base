/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.constvalue.task;

import codedriver.framework.common.constvalue.IEnum;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvzk
 * @since 2021/9/1 16:20
 **/
public enum TaskConfigPolicy implements IEnum {
    ANY("any","任一完成"),MANY("all","所有完成");
    private final String value;
    private final String name;

    TaskConfigPolicy(String _value, String _name) {
        this.value = _value;
        this.name = _name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static String getValue(String _value) {
        for (TaskConfigPolicy s : TaskConfigPolicy.values()) {
            if (s.getValue().equals(_value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getName(String _value) {
        for (TaskConfigPolicy s : TaskConfigPolicy.values()) {
            if (s.getValue().equals(_value)) {
                return s.getName();
            }
        }
        return "";
    }

    @Override
    public List getValueTextList() {
        List<Object> list = new ArrayList<>();
        for(TaskConfigPolicy type : TaskConfigPolicy.values()){
            list.add(new JSONObject(){
                {
                    this.put("value",type.getValue());
                    this.put("text",type.getName());
                }
            });
        }
        return list;
    }
}
