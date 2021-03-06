package mav.com.hheroes.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.User;
import mav.com.hheroes.services.GameService;
import mav.com.hheroes.services.UserService;
import mav.com.hheroes.services.tasks.TaskManager;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private TaskManager taskManager;
	
	@GetMapping("/me")
	public User getMe() {
		return userService.getByEmail(httpSession.getAttribute(GameService.LOGIN).toString()).orElse(null);
	}
	
	@PutMapping
	public void updateUser(@RequestBody User user) {
		userService.update(user);
		if (user.isAutoSalary()) {
			taskManager.addTask(user);
		} else {
			taskManager.removeTask(user.getEmail());
		}
	}
}
