package codedriver.framework.process.dao.mapper;

import codedriver.framework.common.dto.ValueTextVo;
import codedriver.framework.process.dto.PriorityVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PriorityMapper {

	int searchPriorityCount(PriorityVo priorityVo);

	List<PriorityVo> searchPriorityList(PriorityVo priorityVo);

	int searchPriorityCountForMatrix(PriorityVo priorityVo);

	List<PriorityVo> searchPriorityListForMatrix(PriorityVo priorityVo);

	List<ValueTextVo> searchPriorityListForSelect(PriorityVo priorityVo);

	int checkPriorityIsExists(String uuid);

	PriorityVo getPriorityByUuid(String uuid);

	List<PriorityVo> getPriorityByUuidList(List<String> uuidList);

    PriorityVo getPriorityByName(String objValue);

	int checkPriorityNameIsRepeat(PriorityVo priorityVo);

	Integer getMaxSort();

	int checkPriorityIsInvoked(String uuid);

	int insertPriority(PriorityVo priorityVo);

	int updatePriority(PriorityVo priorityVo);
	/**
	 * 
	* @date 2020年7月8日
	* @description 从fromSort到toSort之间（fromSort和toSort）的序号加一
	* @param fromSort
	* @param toSort
	* @return int
	 */
	int updateSortIncrement(@Param("fromSort")Integer fromSort, @Param("toSort")Integer toSort);
	/**
	 * 
	* @date 2020年7月8日
	* @description 从fromSort到toSort之间（fromSort和toSort）的序号减一
	* @param fromSort
	* @param toSort
	* @return int
	 */
	int updateSortDecrement(@Param("fromSort")Integer fromSort, @Param("toSort")Integer toSort);

	int deletePriorityByUuid(String uuid);
}
