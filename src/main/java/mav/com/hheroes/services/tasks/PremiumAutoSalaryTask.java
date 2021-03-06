package mav.com.hheroes.services.tasks;

import java.io.IOException;

import org.apache.log4j.Logger;

import mav.com.hheroes.services.FilleService;
import mav.com.hheroes.services.dtos.UserDTO;
import mav.com.hheroes.services.exceptions.AuthenticationException;

public class PremiumAutoSalaryTask implements Runnable {
	private final Logger logger = Logger.getLogger(getClass());
	
	private FilleService filleService;
	
	private Integer filleId;	
	
	private UserDTO user;
	
	public PremiumAutoSalaryTask() {
	}

	public PremiumAutoSalaryTask(FilleService filleService, Integer girlId, UserDTO user) {
		this.filleService = filleService;
		this.filleId = girlId;
		this.user = user;
	}

	@Override
	public void run() {
		try {
			if (filleService.getGameService().getCookies(user.getLogin()) == null) {
				filleService.getGameService().login(user.getLogin(), user.getPassword());
			}
			
			filleService.collectSalary(filleId, user.getLogin());
		} catch (IOException | AuthenticationException e) {
			logger.error("Something happens while collecting salary of : " + filleId, e);
		}
	}

}
