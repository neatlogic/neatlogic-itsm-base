package codedriver.framework.process.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import codedriver.framework.dto.AuthorityVo;
import codedriver.framework.process.dto.CatalogVo;

public interface CatalogMapper {

	List<CatalogVo> getCatalogList(CatalogVo catalogVo);

	CatalogVo getCatalogByUuid(String uuid);
	
	int getMaxSortByParentUuid(String parentUuid);
	
	int checkCatalogIsExists(String catalogUuid);

	List<String> getHasActiveChannelCatalogUuidList(List<String> channelUuidList);

	int checkCatalogNameIsRepeat(CatalogVo catalogVo);

	List<CatalogVo> getCatalogListForTree(Integer isActive);

	List<AuthorityVo> getCatalogAuthorityListByCatalogUuid(String uuid);
	
	List<String> getAuthorizedCatalogUuidList(
			@Param("userUuid")String userUuid, 
			@Param("teamUuidList")List<String> teamUuidList, 
			@Param("roleNameList")List<String> roleNameList, 
			@Param("catalogUuid") String catalogUuid
			);
	
	int replaceCatalog(CatalogVo catalogVo);

	int insertCatalogAuthority(@Param("authorityVo")AuthorityVo authorityVo,@Param("catalogUuid")String catalogUuid);

	int updateCatalogForMove(CatalogVo catalogVo);

	int updateSortIncrement(@Param("parentUuid")String parentUuid, @Param("fromSort")Integer fromSort, @Param("toSort")Integer toSort);

	int updateSortDecrement(@Param("parentUuid")String parentUuid, @Param("fromSort")Integer fromSort, @Param("toSort")Integer toSort);
	
	int deleteCatalogByUuid(String uuid);

	int deleteCatalogAuthorityByCatalogUuid(String uuid);

}
