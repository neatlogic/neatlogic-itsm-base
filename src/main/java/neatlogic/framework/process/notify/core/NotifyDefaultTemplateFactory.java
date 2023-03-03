/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
