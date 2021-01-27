package codedriver.framework.process.workcenter.dto;

import codedriver.framework.process.workcenter.table.ISqlTable;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Title: JoinTableColumnVo
 * @Package: codedriver.framework.process.workcenter.dto
 * @Description: join Table 关系Vo
 * @Author: 89770
 * @Date: 2021/1/22 16:41
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 **/
public class JoinTableColumnVo {
    private ISqlTable leftTable;
    private String leftTableShortName;
    private ISqlTable rightTable;
    private String rightTableShortName;
    private Map<String, String> onColumnMap;

    public JoinTableColumnVo(ISqlTable _leftTable, ISqlTable _rightTable, Map<String, String> _onColumnMap) {
        construct( _leftTable,  _leftTable.getShortName(),  _rightTable, _rightTable.getShortName(),  _onColumnMap);
    }

    public JoinTableColumnVo(ISqlTable _leftTable, String _leftTableShortName, ISqlTable _rightTable, Map<String, String> _onColumnMap) {
        construct( _leftTable,  _leftTableShortName,  _rightTable, _rightTable.getShortName(),  _onColumnMap);
    }

    public JoinTableColumnVo(ISqlTable _leftTable, ISqlTable _rightTable, String _rightTableShortName, Map<String, String> _onColumnMap) {
        construct( _leftTable, _leftTable.getShortName(),  _rightTable,  _rightTableShortName,  _onColumnMap);
    }

    public JoinTableColumnVo(ISqlTable _leftTable, String _leftTableShortName, ISqlTable _rightTable, String _rightTableShortName, Map<String, String> _onColumnMap) {
        construct( _leftTable,  _leftTableShortName,  _rightTable,  _rightTableShortName,  _onColumnMap);
    }

    private void construct(ISqlTable _leftTable, String _leftTableShortName, ISqlTable _rightTable, String _rightTableShortName, Map<String, String> _onColumnMap){
        this.leftTable = _leftTable;
        this.leftTableShortName = _leftTableShortName;
        this.rightTable = _rightTable;
        this.rightTableShortName = _rightTableShortName;
        this.onColumnMap = _onColumnMap;
    }

    public ISqlTable getLeftTable() {
        return leftTable;
    }

    public void setLeftTable(ISqlTable leftTable) {
        this.leftTable = leftTable;
    }

    public ISqlTable getRightTable() {
        return rightTable;
    }

    public void setRightTable(ISqlTable rightTable) {
        this.rightTable = rightTable;
    }

    public Map<String, String> getOnColumnMap() {
        return onColumnMap;
    }

    public void setOnColumnMap(Map<String, String> onColumnMap) {
        this.onColumnMap = onColumnMap;
    }

    public String getLeftTableShortName() {
        return leftTableShortName;
    }

    public void setLeftTableShortName(String leftTableShortName) {
        this.leftTableShortName = leftTableShortName;
    }

    public String getRightTableShortName() {
        return rightTableShortName;
    }

    public void setRightTableShortName(String rightTableShortName) {
        this.rightTableShortName = rightTableShortName;
    }


    public String toSqlString() {
        List<String> onList = new ArrayList<>();
        for (Map.Entry<String, String> depend : onColumnMap.entrySet()) {
            onList.add(String.format(" %s.%s = %s.%s ", leftTableShortName, depend.getKey(), rightTableShortName, depend.getValue()));
        }
        return String.format(" LEFT JOIN %s %s ON %s", rightTable.getName(), rightTableShortName, String.join(" and ", onList));
    }

    public String getHash() {
        StringBuilder sb = new StringBuilder();
        sb.append(leftTable.getName()).append(rightTable.getName());
        for (Map.Entry<String, String> entry : onColumnMap.entrySet()) {
            sb.append(entry.getKey()).append(entry.getValue());
        }
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes());
    }
}
