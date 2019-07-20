package mav.com.hheroes.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.Opponent;
import mav.com.hheroes.services.GameService;
import mav.com.hheroes.services.TowerFameService;

@RestController
@RequestMapping("/tower")
public class TowerFameController {
	@Autowired
	private TowerFameService towerService;
		
	@Autowired
	private HttpSession httpSession;
	
	@GetMapping("/opponents")
	public List<Opponent> getOpponents() throws IOException {
		return towerService.getOpponents(httpSession.getAttribute(GameService.LOGIN).toString());
	}
}
