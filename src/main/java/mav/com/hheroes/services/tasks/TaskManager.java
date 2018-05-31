package mav.com.hheroes.services.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import mav.com.hheroes.domain.User;
import mav.com.hheroes.services.FilleService;
import mav.com.hheroes.services.dtos.UserDTO;

@Component
public class TaskManager {
	private final Logger logger = Logger.getLogger(getClass());

	@Resource
	private FilleService filleService;

	@Value("${hheroes.rateCollectSalary}")
	private Integer rateCollectSalary;

	private Map<String, ScheduledExecutorService> tasks = new HashMap<>();

	public void addTask(User user) {
		if (tasks.get(user.getEmail()) == null) {
			addNewAutoCollectSalary(user);
		} else if (tasks.get(user.getEmail()).isShutdown()) {
			addNewAutoCollectSalary(user);
		}
	}

	private void addNewAutoCollectSalary(User user) {
		logger.info(String.format("Add salary auto collect to %s", user.getEmail()));
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(
				new AutoSalaryTask(filleService, new UserDTO(user.getEmail(), user.getPassword())), 0,
				rateCollectSalary,
				TimeUnit.MINUTES);
		tasks.put(user.getEmail(), scheduler);
	}

	public void removeTask(String login) {
		logger.info(String.format("Remove salary auto collect to %s", login));
		Optional.ofNullable(tasks.get(login)).ifPresent(ScheduledExecutorService::shutdownNow);
	}

	@PreDestroy
	public void close() {
		tasks.entrySet().stream()
				.map(Entry::getValue)
				.forEach(ScheduledExecutorService::shutdownNow);
	}
}
