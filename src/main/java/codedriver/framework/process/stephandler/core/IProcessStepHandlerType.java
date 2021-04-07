/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.stephandler.core;

/**
* @Author:chenqiwei
* @Time:2020年11月10日
* @ClassName: IProcessStepHandlerType 
* @Description: 流程组件处理器枚举类型接口
 */
public interface IProcessStepHandlerType {

    public String getHandler();

    public String getName();

    public String getType();
}
