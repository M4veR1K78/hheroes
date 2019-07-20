package mav.com.hheroes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import mav.com.hheroes.services.tasks.TaskManager;

@Component
public class AppStartupRunner implements ApplicationRunner {
	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskManager taskManager;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		userService.getWithAutoSalary().stream().forEach(taskManager::addTask);
	}

}
