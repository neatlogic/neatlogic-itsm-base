package codedriver.framework.process.dto;

import java.util.ArrayList;
import java.util.List;


public class ChannelRelationVo {
    private String source;
    private Long channelTypeRelationId;
    private String target;
    private List<String> targetList = new ArrayList<>();
    private List<String> authorityList = new ArrayList<>();
    
//    private transient List<AuthorityVo> authorityVoList;
//    private transient AuthorityVo authorityVo;
    private transient String type;
    private transient String uuid;
    
    public Long getChannelTypeRelationId() {
        return channelTypeRelationId;
    }
    public void setChannelTypeRelationId(Long channelTypeRelationId) {
        this.channelTypeRelationId = channelTypeRelationId;
    }   
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public List<String> getTargetList() {
        return targetList;
    }
    public void setTargetList(List<String> targetList) {
        this.targetList = targetList;
    }

    public List<String> getAuthorityList() {
//        if(authorityList == null && CollectionUtils.isNotEmpty(authorityVoList)) {
//            authorityList = new ArrayList<>();
//            for(AuthorityVo authorityVo : authorityVoList) {
//                GroupSearch groupSearch = GroupSearch.getGroupSearch(authorityVo.getType());
//                if(groupSearch != null) {
//                    authorityList.add(groupSearch.getValuePlugin() + authorityVo.getUuid());
//                }
//            }
//        }
        return authorityList;
    }

    public void setAuthorityList(List<String> authorityList) {
        this.authorityList = authorityList;
    }
    
//    public List<AuthorityVo> getAuthorityVoList() {
//        if(authorityVoList == null && CollectionUtils.isNotEmpty(authorityList)) {
//            authorityVoList = new ArrayList<>();
//            for(String authority : authorityList) {
//                String[] split = authority.split("#");
//                if(GroupSearch.getGroupSearch(split[0]) != null) {
//                    AuthorityVo authorityVo = new AuthorityVo();
//                    authorityVo.setType(split[0]);
//                    authorityVo.setUuid(split[1]);
//                    authorityVoList.add(authorityVo);
//                }
//            }
//        }
//        return authorityVoList;
//    }
//
//    public void setAuthorityVoList(List<AuthorityVo> authorityVoList) {
//        this.authorityVoList = authorityVoList;
//    }
//    public AuthorityVo getAuthorityVo() {
//        return authorityVo;
//    }
//    public void setAuthorityVo(AuthorityVo authorityVo) {
//        this.authorityVo = authorityVo;
//    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
