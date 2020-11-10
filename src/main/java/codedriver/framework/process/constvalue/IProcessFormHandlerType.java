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
}
