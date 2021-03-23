package codedriver.framework.process.formattribute.core;

import codedriver.framework.common.constvalue.ParamType;
import codedriver.framework.process.dto.AttributeDataVo;
import codedriver.framework.process.exception.form.AttributeValidException;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @Author:chenqiwei
 * @Time:2020年11月19日
 * @ClassName: IFormAttributeHandler
 * @Description: 表单组件接口，新增表单组件必须实现此接口
 */
public interface IFormAttributeHandler {
    /** 下拉列表value和text列的组合连接符 **/
    public final static String SELECT_COMPOSE_JOINER = "&=&";

    /**
     * @Author: chenqiwei
     * @Time:2020年11月19日
     * @Description: 获取组件英文名
     * @param @return
     * @return String
     */
    public String getHandler();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月19日
     * @Description: 获取组件中文名
     * @param @return
     * @return String
     */
    public String getHandlerName();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月19日
     * @Description: 获取表单真实控件，不同模式不一样
     * @param @return
     * @return String
     */
    public String getHandlerType(String model);

    /**
     * @Author: chenqiwei
     * @Time:2020年11月19日
     * @Description: 获取组件图标
     * @param @return
     * @return String
     */
    public String getIcon();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月10日
     * @Description: 组件类型，表单组件还是控制组件，form|control
     * @param @return
     * @return String
     */
    public String getType();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月19日
     * @Description: 组件参数的数据类型
     * @param @return
     * @return ParamType
     */
    public ParamType getParamType();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月19日
     * @Description: 组件返回的数据类型
     * @param @return
     * @return String
     */
    public String getDataType();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月19日
     * @Description: 是否需要审计，需要会出现在操作记录列表里
     * @param @return
     * @return boolean
     */
    public boolean isAudit();

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
     * 
     * @Author: chenqiwei
     * @Time:2020年11月19日
     * @Description: 能否作为模板参数
     * @param @return
     * @return boolean
     */
    public boolean isForTemplate();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月10日
     * @Description: 所属模块
     * @param @return
     * @return String
     */
    public String getModule();

    /**
     * @Author: chenqiwei
     * @Time:2020年11月19日
     * @Description: 验证组件数据完整性
     * @param @param
     *            attributeDataVo
     * @param @param
     *            configObj
     * @param @return
     * @param @throws
     *            AttributeValidException
     * @return boolean
     */
    public boolean valid(AttributeDataVo attributeDataVo, JSONObject configObj) throws AttributeValidException;

    /**
     * 
     * @Time:2020年7月10日
     * @Description: 将表单属性值转换成对应的text
     * @param attributeDataVo
     * @param configObj
     * @return Object
     */
    public Object valueConversionText(AttributeDataVo attributeDataVo, JSONObject configObj);

    /**
     * @Description: 将text转换成表单属性值，暂时用于批量导入工单
     * @Author: laiwt
     * @Date: 2021/1/28 17:06
     * @Params: [values, config]
     * @Returns: java.lang.Object
    **/
    public Object textConversionValue(List<String> values,JSONObject config);

    /**
     * @Author: chenqiwei
     * @Time:2020年11月19日
     * @Description: 排序
     * @param @return
     * @return int
     */
    public default int getSort() {
        return 0;
    }

    /**
     * 用于创建索引，不同的表单需根据自身规则分拆成多个field content
     * @param data
     * @return 返回contentList
     */
    public List<String> indexFieldContentList(String data);
}
