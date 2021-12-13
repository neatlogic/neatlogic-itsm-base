/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.exception.processtask;

import codedriver.framework.exception.core.ApiException;
import codedriver.framework.exception.type.PermissionDeniedException;

/**
 * @author linbq
 * @since 2021/12/13 15:34
 **/
public class ProcessTaskViewDeniedException extends PermissionDeniedException {

    private static final long serialVersionUID = 6148939113449322484L;

    public ProcessTaskViewDeniedException(){
        super("当前工单为草稿状态，工单上报成功后才可查看。");
    }

    public ProcessTaskViewDeniedException(String channelName){
        super("查看此工单，需要【" + channelName + "】服务的上报权限，请联系管理员授权。");
    }
}
