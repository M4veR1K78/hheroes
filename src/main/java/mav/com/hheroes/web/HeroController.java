package mav.com.hheroes.web;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.Hero;
import mav.com.hheroes.services.GameService;
import mav.com.hheroes.services.HeroService;
import mav.com.hheroes.services.dtos.StatUpdateResponseDTO;

@RestController
@RequestMapping("/hero")
public class HeroController {
	@Autowired
	private HeroService heroService;
	
	@Autowired
	private HttpSession httpSession;
	
	@GetMapping
	public Hero getHero() throws IOException {
		return heroService.getHero(httpSession.getAttribute(GameService.LOGIN).toString());
	}
	
	@PostMapping("/update")
	public StatUpdateResponseDTO updateStats(@RequestParam("carac") int carac) {
		return heroService.updateStats(carac, 500, httpSession.getAttribute(GameService.LOGIN).toString());
	}
}
