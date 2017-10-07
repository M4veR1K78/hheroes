package mav.com.hheroes.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.services.BossService;
import mav.com.hheroes.services.GameService;
import mav.com.hheroes.services.dtos.UserDTO;
import mav.com.hheroes.services.exceptions.AuthenticationException;

@RestController
@RequestMapping("/")
public class GameController {
	@Resource
	private GameService gameService;
	
	@Resource
	private HttpSession httpSession;
	
	@Resource
	private BossService bossService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(@RequestBody UserDTO user) throws AuthenticationException {
		httpSession.setAttribute(GameService.COOKIE_NAME, gameService.login(user.getLogin(), user.getPassword()));
	}
	
	@RequestMapping(value = "/auth", method = RequestMethod.GET)
	public Boolean isAuthenticated() {
		return httpSession.getAttribute(GameService.COOKIE_NAME) != null;
	}
}
