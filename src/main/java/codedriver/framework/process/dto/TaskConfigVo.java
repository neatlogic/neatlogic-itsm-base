/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.dto.BaseEditorVo;
import codedriver.framework.restful.annotation.EntityField;
import codedriver.framework.util.SnowflakeUtil;

/**
 * @author lvzk
 * @since 2021/9/1 12:11
 **/
public class TaskConfigVo extends BaseEditorVo {
    private static final long serialVersionUID = -6035457128604271114L;
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "任务名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "单人：single, 多人：many", type = ApiParamType.STRING)
    private String type;
    @EntityField(name = "其中一个人完成即可：any,所有人完成：all", type = ApiParamType.STRING)
    private String policy;
    @EntityField(name = "是否激活", type = ApiParamType.INTEGER)
    private int isActive;

    public Long getId() {
        if(id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
