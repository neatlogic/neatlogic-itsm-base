package codedriver.framework.process.exception.form;

import codedriver.framework.exception.core.ApiRuntimeException;

/**
 * @Title: FormAttributeRequiredException
 * @Package codedriver.framework.process.exception.form
 * @Description: TODO
 * @Author: linbq
 * @Date: 2021/1/26 20:45
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 **/
public class FormAttributeRequiredException extends ApiRuntimeException {

    private static final long serialVersionUID = -2778517020610259453L;

    public FormAttributeRequiredException(String label) {
        super("表单属性：'" + label + "'必填");
    }
}
