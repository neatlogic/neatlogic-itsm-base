package codedriver.framework.process.constvalue;

import codedriver.framework.common.constvalue.Expression;
import codedriver.framework.common.constvalue.FormHandlerType;
import codedriver.framework.common.constvalue.ParamType;

/**
 * @Author:chenqiwei
 * @Time:2020年11月10日
 * @ClassName: IProcessFormHandler
 * @Description: 表单组件类型枚举接口
 */
public interface IProcessFormHandlerType {
    /**
     * @Author: chenqiwei
     * @Time:2020年11月10日
     * @Description: 控件
     * @param @return
     * @return String
     */
    public String getHandler();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月10日
     * @Description: 中文名
     * @param @return
     * @return String
     */
    public String getHandlerName();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月10日
     * @Description:
     * @param @return
     * @return String
     */
    public String getDataType();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月10日
     * @Description: 图标
     * @param @return
     * @return String
     */
    public String getIcon();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月10日
     * @Description: 组件类型，表单组件还是控制组件
     * @param @return
     * @return String
     */
    public String getType();

    public FormHandlerType getHandlerType();

    public Expression getExpression();

    public ParamType getParamType();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月10日
     * @Description: 是否可设置为条件
     * @param @return
     * @return boolean
     */
    public boolean isConditionable();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月10日
     * @Description: 是否可设置显示隐藏
     * @param @return
     * @return boolean
     */
    public boolean isShowable();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月10日
     * @Description: 是否可设置赋值
     * @param @return
     * @return boolean
     */
    public boolean isValueable();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月10日
     * @Description: 是否可设置过滤
     * @param @return
     * @return boolean
     */
    public boolean isFilterable();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月10日
     * @Description: 是否有拓展属性
     * @param @return
     * @return boolean
     */
    public boolean isExtendable();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月10日
     * @Description: 所属模块
     * @param @return
     * @return String
     */
    public String getModule();
}
