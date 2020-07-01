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

	public int getMaxRhtCode();

	public int replaceCatalog(CatalogVo catalogVo);

	public int insertCatalogAuthority(@Param("authorityVo")AuthorityVo authorityVo,@Param("catalogUuid")String catalogUuid);

	public int insertRootCatalog();

	public int updateCatalogParentUuidByUuid(CatalogVo catalogVo);

	public int updateCatalogLeftRightCode(@Param("uuid") String uuid, @Param("lft") int lft, @Param("rht") int rht);

	public int batchUpdateCatalogLeftRightCodeByLeftRightCode(@Param("lft") Integer lft, @Param("rht") Integer rht, @Param("step") int step);

	public int batchUpdateCatalogLeftCode(@Param("minCode") Integer minCode, @Param("step") int step);

	public int batchUpdateCatalogRightCode(@Param("minCode") Integer minCode, @Param("step") int step);
	
	public int deleteCatalogByUuid(String uuid);

	public int deleteCatalogAuthorityByCatalogUuid(String uuid);

}
