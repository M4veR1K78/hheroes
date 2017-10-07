package mav.com.hheroes.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.Hero;
import mav.com.hheroes.services.HeroService;

@RestController
@RequestMapping("/hero")
public class HeroController {
	@Autowired
	private HeroService heroService;
	
	@RequestMapping(method = RequestMethod.GET)
	public Hero getHero() throws IOException {
		return heroService.getHero();
	}
}
