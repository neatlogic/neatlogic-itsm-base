package codedriver.framework.process.notify.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import codedriver.framework.process.notify.dto.NotifyTemplateVo;
import codedriver.framework.process.notify.template.IDefaultTemplate;

public class NotifyDefaultTemplateFactory {
	
	public final static String DEFAULT_TEMPLATE_TYPE = "默认";
	
	public final static String DEFAULT_TEMPLATE_UUID_PREFIX = "default_";
	
	private static List<NotifyTemplateVo> defaultTemplateList = new ArrayList<>();
	

	private static int number;
    public static synchronized int nextNum() {
        return number++;
    }
    
	static {
		Reflections reflections = new Reflections("codedriver.framework.process.notify.template");
		Set<Class<? extends IDefaultTemplate>> defaultTemplateClassSet = reflections.getSubTypesOf(IDefaultTemplate.class);
		for(Class<? extends IDefaultTemplate> clazz : defaultTemplateClassSet) {
			try {
				IDefaultTemplate defaultTemplateBase = clazz.newInstance();			
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
								
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
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
	
}
