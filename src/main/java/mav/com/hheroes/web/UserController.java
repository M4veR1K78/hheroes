package mav.com.hheroes.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.User;
import mav.com.hheroes.services.GameService;
import mav.com.hheroes.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Resource
	private UserService userService;
	
	@Resource
	private HttpSession httpSession;
	
	@GetMapping("/me")
	public User getMe() {
		return userService.getByEmail(httpSession.getAttribute(GameService.LOGIN).toString()).orElse(null);
	}
	
	@PutMapping
	public void updateUser(@RequestBody User user) {
		userService.update(user);
	}
}