package codedriver.framework.process.notify.core;

import codedriver.framework.notify.constvalue.CommonNotifyParam;
import codedriver.framework.process.notify.constvalue.ProcessTaskNotifyParam;
import codedriver.framework.process.notify.constvalue.ProcessTaskStepNotifyParam;
import codedriver.framework.process.notify.constvalue.ProcessTaskStepTaskNotifyParam;
import org.springframework.util.ClassUtils;

import codedriver.framework.notify.core.NotifyHandlerType;
import codedriver.framework.process.notify.constvalue.ProcessTaskStepNotifyTriggerType;

public interface IDefaultTemplate {

    String DEFAULT_TEMPLATE_TYPE = "默认";

    String DEFAULT_TEMPLATE_UUID_PREFIX = "default_";

    String OPERATOR = "【" + CommonNotifyParam.OPERATOR.getFreemarkerTemplate() + "】";
    String PROCESSTASK_STEP_NAME = "【" + ProcessTaskStepNotifyParam.STEPNAME.getFreemarkerTemplate() + "】";
    String PROCESSTASK_STEP_WORKER = "【" + ProcessTaskStepNotifyParam.STEPWORKER.getFreemarkerTemplate() + "】";
    String REASON = "【" + "${DATA.reason}" + "】";
//    String SUBTASK_CONTENT = "【" + ProcessTaskParams.SUBTASKCONTENT.getFreemarkerTemplate() + "】";
//    String SUBTASK_WORKER = "【" + ProcessTaskParams.SUBTASKWORKER.getFreemarkerTemplate() + "】";
    String TASK_CONTENT =  ProcessTaskNotifyParam.CONTENT.getFreemarkerTemplate();
    String TASK_USER_CONTENT =  ProcessTaskStepTaskNotifyParam.TASKUSERCONTENT.getFreemarkerTemplate();
    String TASK_WORKER = "【" + ProcessTaskStepTaskNotifyParam.TASKWORKER.getFreemarkerTemplate() + "】";
    String TASK_CONFIG_NAME = ProcessTaskStepTaskNotifyParam.TASKCONFIGNAME.getFreemarkerTemplate();
    String CHANGE_STEP_NAME = "【" + "${DATA.changeStepName}" + "】";
    String CHANGE_STEP_WORKER = "【" + "${DATA.changeStepWorker}" + "】";

    String PROCESSTASK_DETAILS_URL = "${homeUrl}/process.html#/task-detail?processTaskId=${DATA.id}";
    String PROCESSTASK_SERIALNUMBER_TITLE = "【" + ProcessTaskNotifyParam.SERIALNUMBER.getFreemarkerTemplate() + "-" + ProcessTaskNotifyParam.TITLE.getFreemarkerTemplate() + "】";
    String PROCESSTASK_DETAILS_LINK = "查看详情：<a target=\"_blank\" href=" + PROCESSTASK_DETAILS_URL + ">【工单链接】</a>";

    public default Long getId() {
        //return DEFAULT_TEMPLATE_UUID_PREFIX + NotifyDefaultTemplateFactory.nextNum();
        return NotifyDefaultTemplateFactory.nextNum();
    }

    public default String getName() {
        return NotifyHandlerType.getText(getNotifyHandlerType()) + "_" + ProcessTaskStepNotifyTriggerType.getText(getTrigger());
    }

    public default String getType() {
        return DEFAULT_TEMPLATE_TYPE;
    }

    public default int getIsReadOnly() {
        return 1;
    }

    public default String getTrigger() {
        return ClassUtils.getUserClass(this.getClass()).getSimpleName().toLowerCase();
    }

    public String getNotifyHandlerType();

    public String getTitle();

    public String getContent();

    public String description();
}
