package mav.com.hheroes.config;

import javax.annotation.Resource;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mav.com.hheroes.web.filter.AuthenticationFilter;

@Configuration
public class AppConfig {
	@Resource
	private AuthenticationFilter filter;
	
	@Bean
	public FilterRegistrationBean someFilterRegistration() {
	    FilterRegistrationBean registration = new FilterRegistrationBean();
	    registration.setFilter(filter);
	    registration.addUrlPatterns(
	    		"/filles/*", 
	    		"/shop/*", 
	    		"/activity/*", 
	    		"/boss/*", 
	    		"/hero/*");
	    registration.setName("authFilter");
	    return registration;
	} 
}
