package codedriver.framework.process.workcenter.table.constvalue;

/**
 * @Title: FieldTypeEnum
 * @Package: codedriver.framework.process.workcenter.table
 * @Description: TODO
 * @Author: 89770
 * @Date: 2021/1/25 18:18
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 **/
public enum FieldTypeEnum {
    DISTINCT_ID("distinctId", "去重工单ID"), FIELD("field", "选择字段"), TOTAL_COUNT("totalCount", "总个数"), LIMIT_COUNT("count", "部分个数");
    private final String name;
    private final String text;

    private FieldTypeEnum(String _value, String _text){
        this.name = _value;
        this.text = _text;
    }

    public String getValue(){
        return name;
    }

    public String getText(){
        return text;
    }

    public static String getText(String value){
        for (FieldTypeEnum f : FieldTypeEnum.values()){
            if (f.getValue().equals(value)){
                return f.getText();
            }
        }
        return "";
    }
}
