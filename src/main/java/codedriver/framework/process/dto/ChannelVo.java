package codedriver.framework.process.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.common.util.CommonUtil;
import codedriver.framework.dto.AuthorityVo;
import codedriver.framework.restful.annotation.EntityField;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChannelVo extends BasePageVo implements Serializable {

    private static final long serialVersionUID = 7055291271172611259L;

    @EntityField(name = "服务通道uuid", type = ApiParamType.STRING)
    private String uuid;

    @EntityField(name = "服务通道名称", type = ApiParamType.STRING)
    private String name;

    @EntityField(name = "是否启用，0：禁用，1：启用", type = ApiParamType.INTEGER)
    private Integer isActive;

    @EntityField(name = "服务说明", type = ApiParamType.STRING)
    private String desc;

    @EntityField(name = "图标", type = ApiParamType.STRING)
    private String icon;

    @EntityField(name = "颜色", type = ApiParamType.STRING)
    private String color;

    @EntityField(name = "服务目录uuid", type = ApiParamType.STRING)
    private String parentUuid;

    @EntityField(name = "服务所有父目录名，逗号隔开", type = ApiParamType.STRING)
    private String parentNames;

    @EntityField(name = "服务所有父目录uuid，逗号隔开", type = ApiParamType.STRING)
    private String parentUuids;

    @EntityField(name = "是否收藏，0：未收藏，1：已收藏", type = ApiParamType.INTEGER)
    private Integer isFavorite;

    @EntityField(name = "类型", type = ApiParamType.STRING)
    private String type = "channel";

    @EntityField(name = "工作流uuid", type = ApiParamType.STRING)
    private String processUuid;

    @EntityField(name = "服务窗口uuid", type = ApiParamType.STRING)
    private String worktimeUuid;

    @EntityField(name = "优先级uuid列表", type = ApiParamType.JSONARRAY)
    private List<String> priorityUuidList;

    @EntityField(name = "优先级列表", type = ApiParamType.JSONARRAY)
    private List<PriorityVo> priorityList;

    @EntityField(name = "默认优先级", type = ApiParamType.STRING)
    private String defaultPriorityUuid;

    @EntityField(name = "是否显示上报页描述", type = ApiParamType.INTEGER)
    private Integer allowDesc;

    @EntityField(name = "描述帮助", type = ApiParamType.STRING)
    private String help;

    @EntityField(name = "是否激活描述帮助", type = ApiParamType.INTEGER)
    private Integer isActiveHelp;

    @EntityField(name = "时效", type = ApiParamType.INTEGER)
    private Integer sla;

    @EntityField(name = "授权对象", type = ApiParamType.JSONARRAY)
    private List<String> authorityList;

    @EntityField(name = "服务类型uuid", type = ApiParamType.STRING)
    private String channelTypeUuid;

    @EntityField(name = "服务类型", type = ApiParamType.JSONOBJECT)
    private ChannelTypeVo channelTypeVo;

    @EntityField(name = "是否允许转报", type = ApiParamType.INTEGER)
    private int allowTranferReport;

    @EntityField(name = "转报设置列表", type = ApiParamType.JSONARRAY)
    private List<ChannelRelationVo> channelRelationList = new ArrayList<>();

    @EntityField(name = "类型#uuid", type = ApiParamType.STRING)
    private String typeAndUuid;

    @EntityField(name = "使用范围", type = ApiParamType.STRING)
    private String support;

    @EntityField(name = "被引用个数", type = ApiParamType.INTEGER)
    private int childrenCount = 0;

    @JSONField(serialize = false)
    private boolean isAuthority = false;
    @JSONField(serialize = false)
    private List<AuthorityVo> authorityVoList;
    @JSONField(serialize = false)
    private CatalogVo parent;
    @JSONField(serialize = false)
    private Integer sort;
    @JSONField(serialize = false)
    private String userUuid;
    @JSONField(serialize = false)
    private List<String> authorizedUuidList;
    @JSONField(serialize = false)
    private List<String> uuidList;

    public ChannelVo() {

    }

    public ChannelVo(ChannelVo channelVo) {
        this.uuid = channelVo.uuid;
        this.name = channelVo.name;
        this.isActive = channelVo.isActive;
        this.desc = channelVo.desc;
        this.icon = channelVo.icon;
        this.color = channelVo.color;
        this.parentUuid = channelVo.parentUuid;
        this.allowDesc = channelVo.allowDesc;
        this.help = channelVo.help;
        this.isActiveHelp = channelVo.isActiveHelp;
        this.sla = channelVo.sla;
        this.channelTypeUuid = channelVo.channelTypeUuid;
        this.support = channelVo.support;
        this.sort = channelVo.sort;
    }

    public synchronized String getUuid() {
        if (StringUtils.isBlank(uuid)) {
            uuid = UUID.randomUUID().toString().replace("-", "");
        }
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }

    public Integer getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Integer isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public CatalogVo getParent() {
        return parent;
    }

    public void setParent(CatalogVo parent) {
        this.parent = parent;
        parent.addChildChannel(this);
    }

    public String getType() {
        return type;
    }

    public String getProcessUuid() {
        return processUuid;
    }

    public void setProcessUuid(String processUuid) {
        this.processUuid = processUuid;
    }

    public String getWorktimeUuid() {
        return worktimeUuid;
    }

    public void setWorktimeUuid(String worktimeUuid) {
        this.worktimeUuid = worktimeUuid;
    }

    public List<String> getPriorityUuidList() {
        return priorityUuidList;
    }

    public void setPriorityUuidList(List<String> priorityUuidList) {
        this.priorityUuidList = priorityUuidList;
    }

    public String getDefaultPriorityUuid() {
        return defaultPriorityUuid;
    }

    public void setDefaultPriorityUuid(String defaultPriorityUuid) {
        this.defaultPriorityUuid = defaultPriorityUuid;
    }

    public Integer getAllowDesc() {
        return allowDesc;
    }

    public void setAllowDesc(Integer allowDesc) {
        this.allowDesc = allowDesc;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public Integer getIsActiveHelp() {
        return isActiveHelp;
    }

    public void setIsActiveHelp(Integer isActiveHelp) {
        this.isActiveHelp = isActiveHelp;
    }

    public Integer getSla() {
        return sla;
    }

    public void setSla(Integer sla) {
        this.sla = sla;
    }

    public List<String> getAuthorityList() {
        if (authorityList == null && CollectionUtils.isNotEmpty(authorityVoList)) {
            authorityList = new ArrayList<>();
            for (AuthorityVo authorityVo : authorityVoList) {
                GroupSearch groupSearch = GroupSearch.getGroupSearch(authorityVo.getType());
                if (groupSearch != null) {
                    authorityList.add(groupSearch.getValuePlugin() + authorityVo.getUuid());
                }
            }
        }
        return authorityList;
    }

    public void setAuthorityList(List<String> authorityList) {
        this.authorityList = authorityList;
    }

    public List<AuthorityVo> getAuthorityVoList() {
        if (authorityVoList == null && CollectionUtils.isNotEmpty(authorityList)) {
            authorityVoList = new ArrayList<>();
            for (String authority : authorityList) {
                String[] split = authority.split("#");
                if (GroupSearch.getGroupSearch(split[0]) != null) {
                    AuthorityVo authorityVo = new AuthorityVo();
                    authorityVo.setType(split[0]);
                    authorityVo.setUuid(split[1]);
                    authorityVoList.add(authorityVo);
                }
            }
        }
        return authorityVoList;
    }

    public void setAuthorityVoList(List<AuthorityVo> authorityVoList) {
        this.authorityVoList = authorityVoList;
    }

    public String getChannelTypeUuid() {
        return channelTypeUuid;
    }

    public void setChannelTypeUuid(String channelTypeUuid) {
        this.channelTypeUuid = channelTypeUuid;
    }

    public List<String> getAuthorizedUuidList() {
        return authorizedUuidList;
    }

    public void setAuthorizedUuidList(List<String> authorizedUuidList) {
        this.authorizedUuidList = authorizedUuidList;
    }

    public List<String> getUuidList() {
        return uuidList;
    }

    public void setUuidList(List<String> uuidList) {
        this.uuidList = uuidList;
    }

    public String getParentNames() {
        return parentNames;
    }

    public void setParentNames(String parentNames) {
        this.parentNames = parentNames;
    }

    /**
     * 
     * @Time:2020年7月7日
     * @Description: 判断服务是否最终授权，服务状态为激活，拥有服务权限及所有上级目录权限才是最终授权
     * @return boolean
     */
    public boolean isAuthority() {
        if (Objects.equal(isActive, 1) && isAuthority) {
            if (parent != null && !CatalogVo.ROOT_UUID.equals(parent.getUuid())) {
                return parent.isAuthority();
            }
            return true;
        }
        return false;
    }

    public void setAuthority(boolean isAuthority) {
        this.isAuthority = isAuthority;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChannelVo other = (ChannelVo)obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

    public int getAllowTranferReport() {
        return allowTranferReport;
    }

    public void setAllowTranferReport(int allowTranferReport) {
        this.allowTranferReport = allowTranferReport;
    }

    public List<ChannelRelationVo> getChannelRelationList() {
        return channelRelationList;
    }

    public void setChannelRelationList(List<ChannelRelationVo> channelRelationList) {
        this.channelRelationList = channelRelationList;
    }

    public String getTypeAndUuid() {
        if (StringUtils.isBlank(typeAndUuid) && StringUtils.isNotBlank(getUuid())) {
            typeAndUuid = type + "#" + uuid;
        }
        return typeAndUuid;
    }

    public String getSupport() {
        if (support == null) {
            support = CommonUtil.getDevice();
        }
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getParentUuids() {
        return parentUuids;
    }

    public void setParentUuids(String parentUuids) {
        this.parentUuids = parentUuids;
    }

    public List<PriorityVo> getPriorityList() {
        return priorityList;
    }

    public ChannelTypeVo getChannelTypeVo() {
        return channelTypeVo;
    }

    public void setChannelTypeVo(ChannelTypeVo channelTypeVo) {
        this.channelTypeVo = channelTypeVo;
    }

    public void setPriorityList(List<PriorityVo> priorityList) {
        this.priorityList = priorityList;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }
}
