package mav.com.hheroes.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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
	@Resource
	private GameService gameService;
	
	@Resource
	private UserService userService;

	@Resource
	private HttpSession httpSession;

	@PostMapping(value = "/login")
	public void login(@RequestBody UserDTO user, @RequestHeader(value="Accept-Language") String locale) throws AuthenticationException {
		gameService.setLocale(locale);
		httpSession.setAttribute(GameService.COOKIES, gameService.login(user.getLogin(), user.getPassword()));
		httpSession.setAttribute(GameService.LANGUAGE, locale);
		
		/*if (!userService.getByEmail(user.getLogin()).isPresent()) {
			userService.create(new User(user.getLogin()));
		}*/
			
		httpSession.setAttribute(GameService.LOGIN, user.getLogin());
	}

	@GetMapping(value = "/auth")
	public Boolean isAuthenticated() {
		return httpSession.getAttribute(GameService.COOKIES) != null;
	}

	@GetMapping(value = "/image")
	public ResponseEntity<byte[]> getImage(@RequestParam("urlImage") String urlImage) throws IOException {
		byte[] image = gameService.getImage(urlImage);

		String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(image));

		return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(image);
	}
}
