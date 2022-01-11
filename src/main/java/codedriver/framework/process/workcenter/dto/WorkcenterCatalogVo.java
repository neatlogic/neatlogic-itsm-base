package codedriver.framework.process.workcenter.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.EntityField;
import codedriver.framework.util.SnowflakeUtil;

import java.io.Serializable;

/**
 * @author longrf
 * @date 2022/1/10 2:41 下午
 */
public class WorkcenterCatalogVo implements Serializable {
    private static final long serialVersionUID = 1122061508451906324L;

    @EntityField(name = "工单中心类型id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "工单中心类型名称", type = ApiParamType.STRING)
    private String name;

    public Long getId() {
        if (id == null) {
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
}
