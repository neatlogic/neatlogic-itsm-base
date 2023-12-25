package neatlogic.framework.process.spring.configuration;

import neatlogic.framework.common.util.ModuleUtil;
import neatlogic.framework.process.stephandler.core.ProcessStepHandlerBase;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class ItsmBaseConfiguration {

    @Bean
    public InstantiationAwareBeanPostProcessor getItsmInstantiationAwareBeanPostProcessor() {
        return new InstantiationAwareBeanPostProcessor() {
            @Override
            public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
                // 如果没有process模块，则不加载change模块Bean
                if (beanClass.getName().startsWith("neatlogic.module.change")) {
                    if (ModuleUtil.getModuleById("process") == null) {
                        return new Object();
                    }
                }
                // 如果没有process模块，则不加载event模块Bean
                if (beanClass.getName().startsWith("neatlogic.module.event")) {
                    if (ModuleUtil.getModuleById("process") == null) {
                        return new Object();
                    }
                }
                // 如果没有process模块，则不加载eoa模块Bean
                if (beanClass.getName().startsWith("neatlogic.module.eoa")) {
                    if (ModuleUtil.getModuleById("process") == null) {
                        return new Object();
                    }
                }
                // 如果没有process模块，则不加载ProcessStepHandlerBase子类Bean
                if (Objects.equals(beanClass.getSuperclass(), ProcessStepHandlerBase.class)) {
                    if (ModuleUtil.getModuleById("process") == null) {
                        return new Object();
                    }
                }
                return null; // 返回 null 表示不阻止 bean 的实例化
            }
        };
    }
}
