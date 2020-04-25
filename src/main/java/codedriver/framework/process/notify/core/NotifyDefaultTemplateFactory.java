package codedriver.framework.process.notify.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import codedriver.framework.process.notify.dto.NotifyTemplateVo;

public class NotifyDefaultTemplateFactory {

	private final static String DEFAULT_TEMPLATE_TYPE = "默认";
	
	public final static String DEFAULT_TEMPLATE_UUID_PREFIX = "default_";
	
	private final static int DEFAULT_TEMPLATE_IS_READ_ONLY = 1;
	
	private static List<NotifyTemplateVo> defaultTemplateList = new ArrayList<>();
	
	static {
		defaultTemplateList.add(
				new NotifyTemplateVo(DEFAULT_TEMPLATE_UUID_PREFIX + "1", "默认模板1", DEFAULT_TEMPLATE_TYPE, DEFAULT_TEMPLATE_IS_READ_ONLY,
					new StringBuilder(39)//标题
					.append("【ITSM服务单催办】【${task.id}】-【${task.title}】")
					.toString(),
					new StringBuilder(307)//内容
					.append("您好，<br>")
					.append("请注意，用户催办<a href=\"${home}task/getTaskStepDetail.do?processTaskId=${task.id}&processTaskStepId=${step.id}\"><b>【${task.id}】：【${task.title}】</b></a><br><br>")
					.append("请<a href=\"${home}process.html#/task-detail?processTaskId==${task.id}&processTaskStepId=${step.id}\"><b>点击此处</b></a>查看详情，及时派单或处理，并主动告知用户进度，谢谢！<br>")
					.toString()
				)
		);
	}
	
	public static List<NotifyTemplateVo> getDefaultTemplateList() {
		return defaultTemplateList;
	}
	
	public static List<NotifyTemplateVo> getDefaultTemplateList(NotifyTemplateVo notifyTemplateVo) {
		List<NotifyTemplateVo> resultList = new ArrayList<>();
		for(NotifyTemplateVo notifyTemplate : defaultTemplateList) {
			if(notifyTemplateVo != null) {
				if(StringUtils.isNotBlank(notifyTemplateVo.getType()) && !notifyTemplateVo.getType().equals(notifyTemplate.getType())) {
					continue;
				}
				if(StringUtils.isNotBlank(notifyTemplateVo.getKeyword()) && !notifyTemplate.getName().contains(notifyTemplateVo.getKeyword())) {
					continue;
				}
			}
			resultList.add(notifyTemplate);
		}
		return resultList;
	}
	
	public static NotifyTemplateVo getDefaultTemplateByUuid(String uuid) {
		for(NotifyTemplateVo notifyTemplate : defaultTemplateList) {
			if(notifyTemplate.getUuid().equals(uuid)) {
				return notifyTemplate;
			}
		}
		return null;
	}
}
