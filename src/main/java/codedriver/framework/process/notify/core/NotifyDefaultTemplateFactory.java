package codedriver.framework.process.notify.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import codedriver.framework.process.notify.dto.NotifyTemplateVo;

public class NotifyDefaultTemplateFactory {

	public final static String DEFAULT_TEMPLATE_TYPE = "默认";
	
	public final static String DEFAULT_TEMPLATE_UUID_PREFIX = "default_";
	
	private final static int DEFAULT_TEMPLATE_IS_READ_ONLY = 1;
	
	private static List<NotifyTemplateVo> defaultTemplateList = new ArrayList<>();
	
	private static List<DefaultTemplateBase> defaultTemplateBaseList = new ArrayList<>();
	

	private static int number;
    private static synchronized int nextNum() {
        return number++;
    }
    
	static {
		Reflections reflections = new Reflections("codedriver");
		Set<Class<? extends DefaultTemplateBase>> defaultTemplateClassSet = reflections.getSubTypesOf(DefaultTemplateBase.class);
		for(Class<? extends DefaultTemplateBase> clazz : defaultTemplateClassSet) {
			try {
				DefaultTemplateBase defaultTemplateBase = clazz.newInstance();
				if(defaultTemplateBaseList.contains(defaultTemplateBase)) {
					System.out.println("默认模板重复了");
				}else {
					defaultTemplateBaseList.add(defaultTemplateBase);
					defaultTemplateList.add(
							new NotifyTemplateVo(
									defaultTemplateBase.getUuid(), 
									defaultTemplateBase.getName(), 
									defaultTemplateBase.getType(),
									defaultTemplateBase.getIsReadOnly(),
									defaultTemplateBase.getNotifyHandlerType(),
									defaultTemplateBase.getTrigger(),
									defaultTemplateBase.getTitle(),
									defaultTemplateBase.getContent()
							)
					);
				}				
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static List<NotifyTemplateVo> getDefaultTemplateList() {
		return defaultTemplateList;
	}
	
	public static List<NotifyTemplateVo> getDefaultTemplateList(NotifyTemplateVo notifyTemplateVo) {
		List<NotifyTemplateVo> resultList = new ArrayList<>();
		for(NotifyTemplateVo notifyTemplate : defaultTemplateList) {
			if(notifyTemplateVo != null) {
				if(StringUtils.isNotBlank(notifyTemplateVo.getNotifyHandlerType()) && !Objects.equals(notifyTemplateVo.getNotifyHandlerType(), notifyTemplate.getNotifyHandlerType())) {
					continue;
				}
				if(StringUtils.isNotBlank(notifyTemplateVo.getTrigger()) && !Objects.equals(notifyTemplateVo.getTrigger(), notifyTemplate.getTrigger())) {
					continue;
				}
				if(StringUtils.isNotBlank(notifyTemplateVo.getType()) && !Objects.equals(notifyTemplateVo.getType(), notifyTemplate.getType())) {
					continue;
				}
				if(StringUtils.isNotBlank(notifyTemplateVo.getType()) && !Objects.equals(notifyTemplateVo.getType(), notifyTemplate.getType())) {
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
	
	public static NotifyTemplateVo getDefaultTemplateByNotifyHandlerTypeAndTrigger(String notifyHandlerType, String trigger) {
		for(NotifyTemplateVo notifyTemplate : defaultTemplateList) {
			if(Objects.equals(notifyTemplate.getNotifyHandlerType(), notifyHandlerType) 
					&& Objects.equals(notifyTemplate.getTrigger(), trigger)) {
				return notifyTemplate;
			}
		}
		return null;
	}
	
	public static abstract class DefaultTemplateBase {
	    
		public String getUuid() {;
			return DEFAULT_TEMPLATE_UUID_PREFIX + nextNum();
		}
		public abstract String getName();
		public abstract String getNotifyHandlerType();
		public abstract String getTrigger();
		public abstract String getTitle();
		public abstract String getContent();
		
		public String getType() {
			return DEFAULT_TEMPLATE_TYPE;
		}
		public int getIsReadOnly() {
			return DEFAULT_TEMPLATE_IS_READ_ONLY;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(this == obj) {
				return true;
			}
			if(obj == null) {
				return false;
			}
			DefaultTemplateBase other = (DefaultTemplateBase) obj;
			if(!Objects.equals(getNotifyHandlerType(), other.getNotifyHandlerType())) {
				return false;
			}
			if(!Objects.equals(getTrigger(), other.getTrigger())) {
				return false;
			}
			return true;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (getNotifyHandlerType() == null ? 0 : getNotifyHandlerType().hashCode());
			result = prime * result + (getTrigger() == null ? 0 : getTrigger().hashCode());
			return result;
		}
	}
	
	public static class EmailUrge extends DefaultTemplateBase {

		@Override
		public String getName() {
			return "邮件通知催办默认模板";
		}

		@Override
		public String getNotifyHandlerType() {
			return NotifyHandlerType.EMAIL.getValue();
		}

		@Override
		public String getTrigger() {
			return NotifyTriggerType.URGE.getTrigger();
		}

		@Override
		public String getTitle() {
			return new StringBuilder(39)//标题
					.append("【ITSM服务单催办】【${task.id}】-【${task.title}】")
					.toString();
		}

		@Override
		public String getContent() {
			return new StringBuilder(307)//内容
					.append("您好，<br>")
					.append("请注意，用户催办<a href=\"${home}task/getTaskStepDetail.do?processTaskId=${task.id}&processTaskStepId=${step.id}\"><b>【${task.id}】：【${task.title}】</b></a><br><br>")
					.append("请<a href=\"${home}process.html#/task-detail?processTaskId==${task.id}&processTaskStepId=${step.id}\"><b>点击此处</b></a>查看详情，及时派单或处理，并主动告知用户进度，谢谢！<br>")
					.toString();
		}
	}
	
	public static class EmailAbort extends DefaultTemplateBase {

		@Override
		public String getName() {
			return "邮件通知工单取消默认模板";
		}

		@Override
		public String getNotifyHandlerType() {
			return NotifyHandlerType.EMAIL.getValue();
		}

		@Override
		public String getTrigger() {
			return NotifyTriggerType.ABORT.getTrigger();
		}

		@Override
		public String getTitle() {
			return new StringBuilder(39)//标题
					.append("【ITSM服务单催办】【${task.id}】-【${task.title}】")
					.toString();
		}

		@Override
		public String getContent() {
			return new StringBuilder(307)//内容
					.append("您好，<br>")
					.append("请注意，用户催办<a href=\"${home}task/getTaskStepDetail.do?processTaskId=${task.id}&processTaskStepId=${step.id}\"><b>【${task.id}】：【${task.title}】</b></a><br><br>")
					.append("请<a href=\"${home}process.html#/task-detail?processTaskId==${task.id}&processTaskStepId=${step.id}\"><b>点击此处</b></a>查看详情，及时派单或处理，并主动告知用户进度，谢谢！<br>")
					.toString();
		}
	}
}
