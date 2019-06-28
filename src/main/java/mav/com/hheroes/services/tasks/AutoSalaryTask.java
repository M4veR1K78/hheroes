package mav.com.hheroes.services.tasks;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mav.com.hheroes.services.FilleService;
import mav.com.hheroes.services.dtos.UserDTO;
import mav.com.hheroes.services.exceptions.AuthenticationException;

public class AutoSalaryTask implements Runnable {
	private final Logger logger = LogManager.getLogger(getClass());

	private FilleService filleService;

	private UserDTO user;

	public AutoSalaryTask(FilleService filleService, UserDTO user) {
		this.filleService = filleService;
		this.user = user;
	}

	@Override
	public void run() {
		try {
			if (filleService.getGameService().getCookies(user.getLogin()) == null) {
				filleService.getGameService().login(user.getLogin(), user.getPassword());
			}

			filleService.collectAllSalaries(user.getLogin());
		} catch (IOException | AuthenticationException e) {
			logger.error("Something happens while collecting all salaries", e);
		}

	}

}
