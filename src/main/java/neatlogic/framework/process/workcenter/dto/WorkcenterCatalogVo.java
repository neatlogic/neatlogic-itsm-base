package neatlogic.framework.process.workcenter.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;

/**
 * @author longrf
 * @date 2022/1/10 2:41 下午
 */
public class WorkcenterCatalogVo extends BasePageVo {
    private static final long serialVersionUID = 1122061508451906324L;

    @EntityField(name = "nfpwd.workcentercatalogvo.entityfield.id.name", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "nfpwd.workcentercatalogvo.entityfield.name.name", type = ApiParamType.STRING)
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
