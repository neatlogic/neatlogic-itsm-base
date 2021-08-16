/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.notify.core;

import codedriver.framework.applicationlistener.core.ModuleInitializedListenerBase;
import codedriver.framework.bootstrap.CodedriverWebApplicationContext;
import codedriver.framework.common.RootComponent;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RootComponent
public class NotifyDefaultTemplateFactory extends ModuleInitializedListenerBase {

    //	private static List<NotifyTemplateVo> defaultTemplateList = new ArrayList<>();
//
    private static int number;

    public static synchronized long nextNum() {
        return number++;
    }


    private static final Map<String, List<IDefaultTemplate>> templateMap = new HashMap<>();

    public static List<IDefaultTemplate> getTemplate(String handler) {
        return templateMap.get(handler);
    }

    @Override
    public void onInitialized(CodedriverWebApplicationContext context) {
        Map<String, IDefaultTemplate> map = context.getBeansOfType(IDefaultTemplate.class);
        Collection<IDefaultTemplate> values = map.values();
        templateMap.putAll(values.stream().collect(Collectors.groupingBy(IDefaultTemplate::getNotifyHandlerType)));
    }

    @Override
    protected void myInit() {

    }
//    
//	static {
//		Reflections reflections = new Reflections("codedriver.module.process.notify.template");
//		Set<Class<? extends IDefaultTemplate>> defaultTemplateClassSet = reflections.getSubTypesOf(IDefaultTemplate.class);
//		for(Class<? extends IDefaultTemplate> clazz : defaultTemplateClassSet) {
//			try {
//				if(!Modifier.isAbstract(clazz.getModifiers())) {
//					IDefaultTemplate defaultTemplateBase = clazz.newInstance();	
//					defaultTemplateList.add(
//							new NotifyTemplateVo(
//									defaultTemplateBase.getId(), 
//									defaultTemplateBase.getName(), 
//									defaultTemplateBase.getType(),
//									defaultTemplateBase.getIsReadOnly(),
//									defaultTemplateBase.getNotifyHandlerType(),
//									defaultTemplateBase.getTrigger(),
//									defaultTemplateBase.getTitle(),
//									defaultTemplateBase.getContent()
//							)
//					);
//				}				
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	public static List<NotifyTemplateVo> getDefaultTemplateList(NotifyTemplateVo notifyTemplateVo) {
//		List<NotifyTemplateVo> resultList = new ArrayList<>();
//		for(NotifyTemplateVo notifyTemplate : defaultTemplateList) {
//			if(notifyTemplateVo != null) {
//				if(StringUtils.isNotBlank(notifyTemplateVo.getNotifyHandlerType()) && !Objects.equals(notifyTemplateVo.getNotifyHandlerType(), notifyTemplate.getNotifyHandlerType())) {
//					continue;
//				}
//				if(StringUtils.isNotBlank(notifyTemplateVo.getTrigger()) && !Objects.equals(notifyTemplateVo.getTrigger(), notifyTemplate.getTrigger())) {
//					continue;
//				}
//				if(StringUtils.isNotBlank(notifyTemplateVo.getType()) && !Objects.equals(notifyTemplateVo.getType(), notifyTemplate.getType())) {
//					continue;
//				}
//				if(StringUtils.isNotBlank(notifyTemplateVo.getType()) && !Objects.equals(notifyTemplateVo.getType(), notifyTemplate.getType())) {
//					continue;
//				}
//				if(StringUtils.isNotBlank(notifyTemplateVo.getKeyword()) && !notifyTemplate.getName().contains(notifyTemplateVo.getKeyword())) {
//					continue;
//				}
//			}
//			resultList.add(notifyTemplate);
//		}
//		return resultList;
//	}
//	
//	public static NotifyTemplateVo getDefaultTemplateById(String id) {
//		for(NotifyTemplateVo notifyTemplate : defaultTemplateList) {
//			if(notifyTemplate.getId().equals(id)) {
//				return notifyTemplate;
//			}
//		}
//		return null;
//	}
//	
//	public static NotifyTemplateVo getDefaultTemplateByNotifyHandlerTypeAndTrigger(String notifyHandlerType, String trigger) {
//		for(NotifyTemplateVo notifyTemplate : defaultTemplateList) {
//			if(Objects.equals(notifyTemplate.getNotifyHandlerType(), notifyHandlerType) 
//					&& Objects.equals(notifyTemplate.getTrigger(), trigger)) {
//				return notifyTemplate;
//			}
//		}
//		return null;
//	}

}
