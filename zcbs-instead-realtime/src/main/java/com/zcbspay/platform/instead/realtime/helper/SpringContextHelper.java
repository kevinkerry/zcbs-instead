package com.zcbspay.platform.instead.realtime.helper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring容器
 * @author: zhangshd
 * @date:   2017年3月13日 下午3:38:01   
 * @version :v1.0
 */
public class SpringContextHelper implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;
	
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public synchronized static Object getBean(String beanName) {  
        return applicationContext.getBean(beanName);  
    } 
}
