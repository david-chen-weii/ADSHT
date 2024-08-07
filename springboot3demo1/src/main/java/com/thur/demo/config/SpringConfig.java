package com.thur.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.thur.demo.util.Handler;

@Configuration
public class SpringConfig implements WebMvcConfigurer {

	@Autowired
	private Handler handler;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(handler)
				.addPathPatterns("/resetPasswordCheck/reset");

	}
}
