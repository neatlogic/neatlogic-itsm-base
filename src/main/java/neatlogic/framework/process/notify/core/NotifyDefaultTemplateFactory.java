/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.framework.process.notify.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;

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
    public void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IDefaultTemplate> map = context.getBeansOfType(IDefaultTemplate.class);
        Collection<IDefaultTemplate> values = map.values();
        templateMap.putAll(values.stream().collect(Collectors.groupingBy(IDefaultTemplate::getNotifyHandlerType)));
    }

    @Override
    protected void myInit() {

    }
//    
//	static {
//		Reflections reflections = new Reflections("neatlogic.module.process.notify.template");
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
