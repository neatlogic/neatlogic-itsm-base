package codedriver.framework.process.workcenter.table;

/**
 * @Title: SqlTable
 * @Package: codedriver.module.process.workcenter.core.table
 * @Description: TODO
 * @Author: 89770
 * @Date: 2021/1/15 15:43
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 **/
public interface ISqlTable {

    /**
     * @Description: 表名
     * @Author: 89770
     * @Date: 2021/1/15 15:58
     * @Params: []
     * @Returns: java.lang.String
     **/
    public String getName();

    /**
     * @Description: 表缩略名
     * @Author: 89770
     * @Date: 2021/1/15 15:58
     * @Params: []
     * @Returns: java.lang.String
     **/
    public String getShortName();

}
