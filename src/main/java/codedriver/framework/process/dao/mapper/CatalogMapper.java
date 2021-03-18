package codedriver.framework.process.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import codedriver.framework.dto.AuthorityVo;
import codedriver.framework.process.dto.CatalogVo;

public interface CatalogMapper {

	public List<CatalogVo> getCatalogList(CatalogVo catalogVo);

	public CatalogVo getCatalogByUuid(String uuid);
	
	public int checkCatalogIsExists(String catalogUuid);

	public List<String> getHasActiveChannelCatalogUuidList(List<String> channelUuidList);

	public int checkCatalogNameIsRepeat(CatalogVo catalogVo);

	public List<CatalogVo> getCatalogListForTree(@Param("lft") Integer lft, @Param("rht") Integer rht);

	public List<AuthorityVo> getCatalogAuthorityListByCatalogUuid(String uuid);
	
	public List<String> getAuthorizedCatalogUuidList(
			@Param("userUuid")String userUuid, 
			@Param("teamUuidList")List<String> teamUuidList, 
			@Param("roleUuidList")List<String> roleUuidList, 
			@Param("catalogUuid") String catalogUuid
			);

	public String getCatalogLockByUuid(String uuid);

	public List<CatalogVo> getCatalogListByParentUuid(String parentUuid);

	public int checkCatalogIsExistsByLeftRightCode(@Param("uuid")String uuid, @Param("lft") Integer lft, @Param("rht") Integer rht);

	public int getCatalogCount(CatalogVo catalogVo);

	public int getCatalogCountOnLock();
	/**
	 * 
	* @Time:2020年7月7日
	* @Description: 根据左右编码查出目录及所有上级目录
	* @param lft 左编码
	* @param rht 右编码
	* @return List<CatalogVo>
	 */
	public List<CatalogVo> getAncestorsAndSelfByLftRht(@Param("lft") Integer lft, @Param("rht") Integer rht);
	
	/**
	 * 根据父uuid获取授权的子服务目录列表
	 * @param uuid
	 * @return List<CatalogVo>
	 */
    public List<CatalogVo> getAuthorizedCatalogList(
            @Param("userUuid")String userUuid,
            @Param("teamUuidList")List<String> teamUuidList,
            @Param("roleUuidList")List<String> roleUuidList,
            @Param("parentUuid") String parentUuid,
            @Param("uuid") String uuid
    );

    public Integer getMaxRhtCode();
    
    /**
     * 
    * @Time:2020年7月20日
    * @Description: 判断左右编码是否全部正确，符合下列条件的才正确
    * 1.左右编码不能为null
    * 2.左编码不能小于2，右编码不能小于3
    * 3.子节点的左编码大于父节点的左编码，子节点的右编码小于父节点的右编码
    * 4.没有子节点的节点左编码比右编码小1
    * @return int 返回左右编码不正确的个数
     */
    public int checkLeftRightCodeIsWrong();

    public List<String> getCatalogUuidListByLftRht(@Param("lft") Integer lft, @Param("rht")Integer rht);

	public int insertCatalog(CatalogVo catalogVo);

	public int insertCatalogAuthority(@Param("authorityVo")AuthorityVo authorityVo,@Param("catalogUuid")String catalogUuid);

	public int updateCatalogParentUuidByUuid(CatalogVo catalogVo);

	public int updateCatalogLeftRightCode(@Param("uuid") String uuid, @Param("lft") int lft, @Param("rht") int rht);

	public int batchUpdateCatalogLeftRightCodeByLeftRightCode(@Param("lft") Integer lft, @Param("rht") Integer rht, @Param("step") int step);

	public int batchUpdateCatalogLeftCode(@Param("minCode") Integer minCode, @Param("step") int step);

	public int batchUpdateCatalogRightCode(@Param("minCode") Integer minCode, @Param("step") int step);

	public int updateCatalogByUuid(CatalogVo catalogVo);
	
	public int deleteCatalogByUuid(String uuid);

	public int deleteCatalogAuthorityByCatalogUuid(String uuid);
}
