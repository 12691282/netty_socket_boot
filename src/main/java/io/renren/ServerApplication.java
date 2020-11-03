/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren;

import io.renren.common.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
@Slf4j
public class ServerApplication {

	public static void main(String[] args) {
		//run方法的返回值ConfigurableApplicationContext继承了ApplicationContext上下文接口
		ConfigurableApplicationContext applicationContext = SpringApplication.run(ServerApplication.class, args);
		BeanUtils.setApplicationContext(applicationContext);
		log.info("The netty server started !");
	}

}