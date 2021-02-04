package codedriver.framework.process.workcenter.dto;

/**
 * @Title: JoinTableColumnVo
 * @Package: codedriver.framework.process.workcenter.dto
 * @Description: join Table 关系Vo
 * @Author: 89770
 * @Date: 2021/1/22 16:41
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 **/
public class SelectColumnVo {
    private String columnName;
    private String propertyName;

    public SelectColumnVo(String _columnName , String _propertyName) {
       this.columnName = _columnName;
       this.propertyName = _propertyName;
    }

    public SelectColumnVo(String _columnName) {
        this.columnName = _columnName;
        this.propertyName = _columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}
