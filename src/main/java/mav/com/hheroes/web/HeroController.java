package mav.com.hheroes.web;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.Hero;
import mav.com.hheroes.services.GameService;
import mav.com.hheroes.services.HeroService;

@RestController
@RequestMapping("/hero")
public class HeroController {
	@Autowired
	private HeroService heroService;
	
	@Resource
	private HttpSession httpSession;
	
	@RequestMapping(method = RequestMethod.GET)
	public Hero getHero() throws IOException {
		return heroService.getHero(httpSession.getAttribute(GameService.LOGIN).toString());
	}
}
