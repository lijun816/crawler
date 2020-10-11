package cn.lijun816.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringBeanUtil
 *
 * @author 87922 2020/10/11
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return ctx.getBean(requiredType);
    }
}
