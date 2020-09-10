package codedriver.framework.process.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Objects;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.dto.AuthorityVo;
import codedriver.framework.restful.annotation.EntityField;

public class CatalogVo extends BasePageVo implements Comparable<CatalogVo>,Serializable{

    private static final long serialVersionUID = 9045187703084309757L;
    public final static String ROOT_UUID = "0";
	public final static String ROOT_PARENTUUID = "-1";
	public final static String UNCATEGORIZED_CATALOG_UUID = "1";
	@EntityField(name = "服务目录uuid", type = ApiParamType.STRING)
	private String uuid;
	
	@EntityField(name = "服务目录名称", type = ApiParamType.STRING)
	private String name;
	
	@EntityField(name = "服务目录父级uuid", type = ApiParamType.STRING)
	private String parentUuid;
	
	@EntityField(name = "是否启用，0：禁用，1：启用", type = ApiParamType.INTEGER)
	private Integer isActive;
	
	@EntityField(name = "图标", type = ApiParamType.STRING)
	private String icon;
	
	@EntityField(name = "颜色", type = ApiParamType.STRING)
	private String color;
	
	@EntityField(name = "描述", type = ApiParamType.STRING)
	private String desc;
	
	@EntityField(name = "子目录或通道", type = ApiParamType.JSONARRAY)
	private List<Object> children = new ArrayList<>();
	
	@EntityField(name = "类型", type = ApiParamType.STRING)
	private String type = "catalog";
	
	@EntityField(name = "授权对象", type = ApiParamType.JSONARRAY)
	private List<String> authorityList;
	
	@EntityField(name = "左编码", type = ApiParamType.INTEGER)
	private Integer lft;
	@EntityField(name = "右编码", type = ApiParamType.INTEGER)
	private Integer rht;

	@EntityField(name = "子节点数", type = ApiParamType.INTEGER)
	private int childrenCount = 0;
	
	@EntityField(name = "类型#uuid", type = ApiParamType.STRING)
	private String typeAndUuid;
	
	private transient boolean isAuthority = false;
	
	private transient List<AuthorityVo> authorityVoList;
	
	private transient CatalogVo parent;
	
	private transient List<Integer> sortList;
	
	private transient List<String> nameList;
	
	private transient List<CatalogVo> childCatalogList = new ArrayList<>();
	
	private transient List<ChannelVo> childChannelList = new ArrayList<>();

	public CatalogVo() {
	}
	
	public CatalogVo(String uuid) {
		this.uuid = uuid;
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

	public String getParentUuid() {
		return parentUuid;
	}

	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}
	
	public Integer getIsActive() {
		return isActive;
	}
	
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
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
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Object> getChildren() {
		children.clear();
		children.addAll(childCatalogList);
		children.addAll(childChannelList);
		return children;
	}

	public boolean addChildCatalog(CatalogVo catalogVo) {
		if(childCatalogList.contains(catalogVo)) {
			return false;
		}
		childrenCount++;
		return childCatalogList.add(catalogVo);
	}
	public boolean removeChildCatalog(CatalogVo catalogVo) {
		if(CollectionUtils.isNotEmpty(childCatalogList)) {
			Iterator<CatalogVo> iterator = childCatalogList.iterator();
			while(iterator.hasNext()) {
				if(iterator.next().equals(catalogVo)) {
					iterator.remove();
					childrenCount--;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean addChildChannel(ChannelVo channelVo) {
		if(childChannelList.contains(channelVo)) {
			return false;
		}
		childrenCount++;
		return childChannelList.add(channelVo);
	}
	public boolean removeChildChannel(ChannelVo channelVo) {
		if(CollectionUtils.isNotEmpty(childChannelList)) {
			Iterator<ChannelVo> iterator = childChannelList.iterator();
			while(iterator.hasNext()) {
				if(iterator.next().equals(channelVo)) {
					iterator.remove();
					childrenCount--;
					return true;
				}
			}
		}
		return false;
	}

	public CatalogVo getParent() {
		return parent;
	}

	public void setParent(CatalogVo parent) {
		this.parent = parent;
		parent.addChildCatalog(this);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getAuthorityList() {
		if(authorityList == null && CollectionUtils.isNotEmpty(authorityVoList)) {
			authorityList = new ArrayList<>();
			for(AuthorityVo authorityVo : authorityVoList) {
				GroupSearch groupSearch = GroupSearch.getGroupSearch(authorityVo.getType());
				if(groupSearch != null) {
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
		if(authorityVoList == null && CollectionUtils.isNotEmpty(authorityList)) {
			authorityVoList = new ArrayList<>();
			for(String authority : authorityList) {
				String[] split = authority.split("#");
				if(GroupSearch.getGroupSearch(split[0]) != null) {
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

	public int getChildrenCount() {
		return childrenCount;
	}

	public void setChildrenCount(int childrenCount) {
		this.childrenCount = childrenCount;
	}

	public List<Integer> getSortList() {
		if(sortList != null) {
			return sortList;
		}
		if(parent != null) {
			sortList = new ArrayList<>(parent.getSortList());			
		}else {
			sortList = new ArrayList<>();
		}		
		sortList.add(lft);
		return sortList;
	}

	public List<String> getNameList() {
		if(nameList != null) {
			return nameList;
		}
		if(parent != null && !CatalogVo.ROOT_UUID.equals(parent.getUuid())) {
			nameList = new ArrayList<>(parent.getNameList());
		}else {
			nameList = new ArrayList<>();
		}
		nameList.add(name);
		return nameList;
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
		CatalogVo other = (CatalogVo) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	public Integer getLft() {
		return lft;
	}

	public void setLft(Integer lft) {
		this.lft = lft;
	}

	public Integer getRht() {
		return rht;
	}

	public void setRht(Integer rht) {
		this.rht = rht;
	}

	/**
	 * 
	* @Time:2020年7月7日
	* @Description: 判断目录是否最终授权，目录状态为激活，拥有目录权限及所有上级目录权限才是最终授权
	* @return boolean
	 */
	public boolean isAuthority() {
		if(Objects.equal(isActive, 1) && isAuthority) {
			if(parent != null && !CatalogVo.ROOT_UUID.equals(parent.getUuid())) {
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
	public int compareTo(CatalogVo other) {
		List<Integer> sortList1 = this.getSortList();
		List<Integer> sortList2 = other.getSortList();
		int size1 = sortList1.size();
		int size2 = sortList2.size();
		int minIndex = 0;
		int resultDefault = 0;
		if(size1 > size2) {
			minIndex = size2;
			resultDefault = 1;
		}else {
			minIndex = size1;
			resultDefault = -1;
		}
		for(int i = 0; i < minIndex; i++) {
			if(Objects.equal(sortList1.get(i), sortList2.get(i))) {
				continue;
			}else {
				int sort1 = sortList1.get(i) == null ? 0 : sortList1.get(i);
				int sort2 = sortList2.get(i) == null ? 0 : sortList2.get(i);
				return sort1 - sort2;
			}
		}
		return resultDefault;
	}

    public String getTypeAndUuid() {
        if(StringUtils.isBlank(typeAndUuid) && StringUtils.isNotBlank(getUuid())) {
            typeAndUuid = type + "#" + uuid;
        }
        return typeAndUuid;
    }

}
