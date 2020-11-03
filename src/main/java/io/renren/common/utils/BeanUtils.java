package io.renren.common.utils;

import org.springframework.context.ConfigurableApplicationContext;

/**
 * 获取bean对象
 * @author
 */
public class BeanUtils {
    /**
     * 将管理上下文的applicationContext设置成静态变量，供全局调用
     */
    private static ConfigurableApplicationContext applicationContext;


    public static ConfigurableApplicationContext  getApplicationContext(){
        return applicationContext;
    }

    public static void setApplicationContext(ConfigurableApplicationContext applicationContext){
        BeanUtils.applicationContext = applicationContext;
    }

    public static Boolean isApplicationContextReady(){
        return BeanUtils.applicationContext == null;
    }


    /**
     *定义一个获取已经实例化bean的方法
     */
    public static <T> T getBean(Class<T> c){
        return applicationContext.getBean(c);
    }

}
