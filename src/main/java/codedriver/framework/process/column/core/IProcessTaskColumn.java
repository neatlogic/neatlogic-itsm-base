package codedriver.framework.process.column.core;

import codedriver.framework.dashboard.dto.DashboardDataVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.workcenter.dto.JoinTableColumnVo;
import codedriver.framework.process.workcenter.dto.TableSelectColumnVo;
import codedriver.framework.process.workcenter.dto.WorkcenterVo;
import codedriver.framework.process.workcenter.table.ISqlTable;

import java.util.List;
import java.util.Map;

public interface IProcessTaskColumn {

    /**
     * @Description: 字段英文名
     * @Param:
     * @return: java.lang.String
     * @Date: 2020/2/2
     */
    public String getName();

    /**
     * @Description: 字段显示名
     * @Param:
     * @return: java.lang.String
     * @Date: 2020/2/2
     */
    public String getDisplayName();

    /**
     * @Description: 字段是否允许排序
     * @Param:
     * @return: java.lang.String
     * @Date: 2020/2/2
     */
    public Boolean allowSort();

    /**
     * @Description: 获取显示值 workcenter
     * @Param:
     * @return: java.lang.Object
     * @Date: 2020/2/2
     */
//    public Object getValue(MultiAttrsObject el) throws RuntimeException;

    /**
     * @Description: 获取显示值 dashboard
     * @Param:
     * @return: java.lang.Object
     * @Date: 2020/2/2
     */
//    public Object getValueText(MultiAttrsObject el) throws RuntimeException;

    /**
     * @Description: 获取类型
     * @Param:
     * @return: java.lang.String
     * @Date: 2020/3/25
     */
    public String getType();

    /**
     * @Description: 获取展示字段样式
     * @Param:
     * @return: java.lang.String
     * @Date: 2020/3/26
     */
    public String getClassName();

    /**
     * @Description: 获取展示字段默认顺序
     * @Param:
     * @return: java.lang.String
     * @Date: 2020/3/27
     */
    public Integer getSort();


    /**
     * @Description: 是否显示该字段
     * @Param:
     * @return: java.lang.String
     * @Date: 2020/6/5
     */
    public Boolean getIsShow();

    //Object getMyValue(JSONObject json);

    public Boolean getDisabled();

    /**
     * @return
     * @Author 89770
     * @Time 2020年10月19日
     * @Description: 是否需要按该字段排序工单数据
     * @Param
     */
    public Boolean getIsSort();

    /**
     * 暂时用来从getValue()返回的数据抽取导出excel时需要的数据
     *
     * @param json
     * @return
     */
    //Object getSimpleValue(Object json);

    /**
     * @return
     * @Author 89770
     * @Time 2020年12月3日
     * @Description: 导出excel，是否需要导出该字段
     * @Param
     */
    public Boolean getIsExport();

    /**
     * @Description: 获取需要关联的表和字段
     * @Author: 89770
     * @Date: 2021/1/26 10:41
     * @Params: []
     * @Returns: java.util.List<codedriver.framework.process.workcenter.dto.JoinTableColumnVo>
     **/
    public List<JoinTableColumnVo> getJoinTableColumnList();

    /**
     * @Description: 主table  用于sort distinct
     * @Author: 89770
     * @Date: 2021/1/26 12:08
     * @Params: []
     * @Returns: codedriver.framework.process.workcenter.table.ISqlTable
     **/
    public ISqlTable getSortSqlTable();

    /**
     * @Description: 主column 用于sort distinct
     * @Author: 89770
     * @Date: 2021/1/26 12:08
     * @Params: []
     * @Returns: java.lang.String
     **/
    public String getSortSqlColumn();

    /**
     * @Description: 重新渲染字段
     * @Author: 89770
     * @Date: 2021/1/26 15:29
     * @Params: [processTaskVo]
     * @Returns: java.lang.Object
     **/
    public Object getValue(ProcessTaskVo processTaskVo);

    /**
     * @Description: 重新渲染字段 (主要获取字符串 用于导出)
     * @Author: 89770
     * @Date: 2021/1/26 15:29
     * @Params: [processTaskVo]
     * @Returns: java.lang.Object
     **/
    public String getSimpleValue(ProcessTaskVo processTaskVo);

    /**
     * @Description: 获取table 需要 select 出来的 column
     * @Author: 89770
     * @Date: 2021/1/26 16:27
     * @Params: [workcenterVo]
     * @Returns: java.util.Map<codedriver.framework.process.workcenter.table.ISqlTable, java.lang.String>
     **/
    public List<TableSelectColumnVo> getTableSelectColumn();

    /**
     * @Description: 获取dashboard 映射关系
     * @Author: 89770
     * @Date: 2021/3/5 17:01
     * @Params: [mapList]
     * @Returns: com.alibaba.fastjson.JSONArray
     **/
    public void getDashboardDataVo(DashboardDataVo dashboardDataVo, WorkcenterVo workcenterVo, List<Map<String, Object>> mapList);

    /**
     * @Description: 将数据集提取groupList, 用于过滤，并按list 权重排序
     * @Author: 89770
     * @Date: 2021/3/5 17:05
     * @Params: [mapList]
     * @Returns: java.util.List<java.lang.String>
     **/
    void getExchangeToDashboardGroupDataMap(List<Map<String, Object>> mapList, WorkcenterVo workcenterVo);
}
