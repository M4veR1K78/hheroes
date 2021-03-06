package mav.com.hheroes.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.User;
import mav.com.hheroes.services.GameService;
import mav.com.hheroes.services.UserService;
import mav.com.hheroes.services.dtos.UserDTO;
import mav.com.hheroes.services.exceptions.AuthenticationException;

@RestController
@RequestMapping("/")
public class GameController {
	@Autowired
	private GameService gameService;

	@Autowired
	private UserService userService;

	@Autowired
	private HttpSession httpSession;

	@PostMapping(value = "/login")
	public void login(@RequestBody UserDTO user, @RequestHeader(value = "Accept-Language") String locale)
			throws AuthenticationException {
		gameService.setLocale(locale);
		httpSession.setAttribute(GameService.COOKIES, gameService.login(user.getLogin(), user.getPassword()));
		httpSession.setAttribute(GameService.LANGUAGE, locale);

		if (!userService.getByEmail(user.getLogin()).isPresent()) {
			userService.create(new User(user.getLogin(), user.getPassword()));
		}

		httpSession.setAttribute(GameService.LOGIN, user.getLogin());
	}

	@GetMapping(value = "/auth")
	public Boolean isAuthenticated() {
		return httpSession.getAttribute(GameService.COOKIES) != null;
	}

	@GetMapping(value = "/image")
	public ResponseEntity<byte[]> getImage(@RequestParam("urlImage") String urlImage) throws IOException {
		return returnImage(gameService.getImage(urlImage, httpSession.getAttribute(GameService.LOGIN).toString()));
	}
	
	@GetMapping(value = "/position")
	public ResponseEntity<byte[]> getPosition(@RequestParam("image") String urlImage) throws IOException {
		return returnImage(gameService.getPosition(urlImage, httpSession.getAttribute(GameService.LOGIN).toString()));
	}
	
	@GetMapping("/logout")
	public void logout() throws IOException {
		gameService.logout(httpSession.getAttribute(GameService.LOGIN).toString());
		httpSession.setAttribute(GameService.COOKIES, null);
	}

	private ResponseEntity<byte[]> returnImage(byte[] image) throws IOException {
		if (image != null) {
			return ResponseEntity.ok().body(image);
		} else {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
		}
	}
}
