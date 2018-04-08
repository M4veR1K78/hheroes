package mav.com.hheroes.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@EnableScheduling
public class JobExecutorConfig {
	private static final Integer nbThread = 6;
	
	@Bean
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(nbThread);
		executor.setMaxPoolSize(nbThread);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("TaskExecutor-");
		executor.initialize();
		return executor;
	}
}
