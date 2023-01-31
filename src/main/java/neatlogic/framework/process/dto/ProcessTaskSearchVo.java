/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.dto;

import neatlogic.framework.common.dto.BasePageVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linbq
 * @since 2021/9/15 14:26
 **/
public class ProcessTaskSearchVo extends BasePageVo {
    private List<String> includeChannelUuidList;
    private List<Long> excludeIdList;
    private String excludeStatus;

    public void setIncludeChannelUuid(String includeChannelUuid) {
        if (includeChannelUuidList == null) {
            includeChannelUuidList = new ArrayList<>();
        }
        includeChannelUuidList.add(includeChannelUuid);
    }

    public List<String> getIncludeChannelUuidList() {
        return includeChannelUuidList;
    }

    public void setIncludeChannelUuidList(List<String> includeChannelUuidList) {
        this.includeChannelUuidList = includeChannelUuidList;
    }

    public List<Long> getExcludeIdList() {
        return excludeIdList;
    }

    public void setExcludeIdList(List<Long> excludeIdList) {
        this.excludeIdList = excludeIdList;
    }

    public String getExcludeStatus() {
        return excludeStatus;
    }

    public void setExcludeStatus(String excludeStatus) {
        this.excludeStatus = excludeStatus;
    }
}
