package neatlogic.framework.process.workcenter.dto;

import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.process.column.core.IProcessTaskColumn;
import neatlogic.framework.util.SpringContextUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.context.support.StaticMessageSource;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Locale;

/** 验证工作台表头直接使用列组件返回的最终展示文案。 */
public class WorkcenterTheadVoTest {

    /** 已翻译的列名称不应再次作为国际化键查询。 */
    @Test
    public void shouldUseLocalizedColumnDisplayNameDirectly() throws Exception {
        Field contextField = SpringContextUtil.class.getDeclaredField("ctx");
        contextField.setAccessible(true);
        Object originalContext = contextField.get(null);
        Locale originalLocale = Locale.getDefault();
        UserContext originalUserContext = UserContext.get();
        try {
            StaticMessageSource source = new StaticMessageSource();
            source.addMessage("Status", Locale.ENGLISH, "错误的二次翻译");
            StaticApplicationContext context = new StaticApplicationContext();
            context.getBeanFactory().registerSingleton("messageSourceAccessor", new MessageSourceAccessor(source));
            new SpringContextUtil().setApplicationContext(context);
            Locale.setDefault(Locale.ENGLISH);
            UserContext.init((UserContext) null).setUserUuid("test-user");
            IProcessTaskColumn column = (IProcessTaskColumn) Proxy.newProxyInstance(
                    IProcessTaskColumn.class.getClassLoader(),
                    new Class<?>[]{IProcessTaskColumn.class},
                    (proxy, method, args) -> {
                        switch (method.getName()) {
                            case "getName":
                                return "status";
                            case "getDisplayName":
                                return "Status";
                            case "getSort":
                                return 1;
                            case "getDisabled":
                                return false;
                            case "getIsExport":
                            case "getIsShow":
                            case "getIsSort":
                                return true;
                            default:
                                return null;
                        }
                    });

            Assert.assertEquals("Status", new WorkcenterTheadVo(column).getDisplayName());
        } finally {
            if (UserContext.get() != null) {
                UserContext.get().release();
            }
            if (originalUserContext != null) {
                UserContext.init(originalUserContext);
            }
            contextField.set(null, originalContext);
            Locale.setDefault(originalLocale);
        }
    }
}
