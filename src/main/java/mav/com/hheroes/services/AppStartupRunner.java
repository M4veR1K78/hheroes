package mav.com.hheroes.services;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import mav.com.hheroes.services.tasks.TaskManager;

@Component
public class AppStartupRunner implements ApplicationRunner {
	@Resource
	private UserService userService;
	
	@Resource
	private TaskManager taskManager;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		userService.getWithAutoSalary().stream().forEach(taskManager::addTask);
	}

}
