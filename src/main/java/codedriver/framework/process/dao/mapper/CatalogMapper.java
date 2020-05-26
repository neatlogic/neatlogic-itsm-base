package codedriver.framework.process.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import codedriver.framework.dto.AuthorityVo;
import codedriver.framework.process.dto.CatalogVo;

public interface CatalogMapper {

	List<CatalogVo> getCatalogList(CatalogVo catalogVo);

	CatalogVo getCatalogByUuid(String uuid);
	
//	int getMaxSortByParentUuid(String parentUuid);
	
	int checkCatalogIsExists(String catalogUuid);

	List<String> getHasActiveChannelCatalogUuidList(List<String> channelUuidList);

	int checkCatalogNameIsRepeat(CatalogVo catalogVo);

	List<CatalogVo> getCatalogListForTree(Integer isActive);

	List<AuthorityVo> getCatalogAuthorityListByCatalogUuid(String uuid);
	
	List<String> getAuthorizedCatalogUuidList(
			@Param("userUuid")String userUuid, 
			@Param("teamUuidList")List<String> teamUuidList, 
			@Param("roleUuidList")List<String> roleUuidList, 
			@Param("catalogUuid") String catalogUuid
			);

	String getCatalogLockByUuid(String uuid);

	List<CatalogVo> getCatalogByParentUuid(String parentUuid);

	int checkCatalogIsExistsByLeftRightCode(@Param("uuid")String uuid, @Param("lft") Integer lft, @Param("rht") Integer rht);

	int getCatalogCount(CatalogVo catalogVo);
	
	int replaceCatalog(CatalogVo catalogVo);

	int insertCatalogAuthority(@Param("authorityVo")AuthorityVo authorityVo,@Param("catalogUuid")String catalogUuid);

	int updateCatalogParentUuidByUuid(CatalogVo catalogVo);

//	int updateSortIncrement(@Param("parentUuid")String parentUuid, @Param("fromSort")Integer fromSort, @Param("toSort")Integer toSort);

//	int updateSortDecrement(@Param("parentUuid")String parentUuid, @Param("fromSort")Integer fromSort, @Param("toSort")Integer toSort);

	int updateCatalogLeftRightCode(@Param("uuid") String uuid, @Param("lft") int lft, @Param("rht") int rht);

	int batchUpdateCatalogLeftRightCodeByLeftRightCode(@Param("lft") Integer lft, @Param("rht") Integer rht, @Param("step") int step);

	int batchUpdateCatalogLeftCode(@Param("minCode") Integer minCode, @Param("step") int step);

	int batchUpdateCatalogRightCode(@Param("minCode") Integer minCode, @Param("step") int step);
	
	int deleteCatalogByUuid(String uuid);

	int deleteCatalogAuthorityByCatalogUuid(String uuid);

}
