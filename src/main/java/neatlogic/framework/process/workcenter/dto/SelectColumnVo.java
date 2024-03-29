package neatlogic.framework.process.workcenter.dto;

import org.apache.commons.lang3.StringUtils;

/**
 * @Title: JoinTableColumnVo
 * @Package: neatlogic.framework.process.workcenter.dto
 * @Description: join Table 关系Vo
 * @Author: 89770
 * @Date: 2021/1/22 16:41
 **/
public class SelectColumnVo {
    private String columnName;//数据库字段名
    private String propertyName;//实体字段名
    private Boolean isPrimary;//是否主键
    private String format;//字段格式化，如"DATE_FORMAT(%s%s,'%%Y-%%m-%%e')"

    public SelectColumnVo(String _columnName, String _propertyName) {
        this.columnName = _columnName;
        this.propertyName = _propertyName;
        this.isPrimary = false;
    }

    public SelectColumnVo(String _columnName) {
        this.columnName = _columnName;
        this.propertyName = _columnName;
        this.isPrimary = false;
    }

    public SelectColumnVo(String _columnName, String _propertyName, Boolean _isPrimary) {
        this.columnName = _columnName;
        this.propertyName = _propertyName;
        this.isPrimary = _isPrimary;
    }

    public SelectColumnVo(String _columnName, String _propertyName, Boolean _isPrimary, String _format) {
        this.columnName = _columnName;
        this.propertyName = _propertyName;
        this.isPrimary = _isPrimary;
        this.format = _format;
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

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getColumnFormat(){
        if(StringUtils.isNotBlank(format)) {
            return String.format("%s as %s", format, propertyName);
        }else {
            return null;
        }
    }
}
