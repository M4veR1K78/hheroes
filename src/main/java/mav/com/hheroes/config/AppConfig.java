package mav.com.hheroes.config;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mav.com.hheroes.web.filter.AuthenticationFilter;

@Configuration
public class AppConfig {
	@Autowired
	private AuthenticationFilter filter;
	
	@Bean
	public FilterRegistrationBean<? super Filter> someFilterRegistration() {
	    FilterRegistrationBean<? super Filter> registration = new FilterRegistrationBean<>();
	    registration.setFilter(filter);
	    registration.addUrlPatterns(
	    		"/filles/*", 
	    		"/shop/*", 
	    		"/activity/*", 
	    		"/boss/*", 
	    		"/hero/*",
	    		"/tower/*",
	    		"/arena/*",
	    		"/champion/*");
	    registration.setName("authFilter");
	    return registration;
	} 
}
