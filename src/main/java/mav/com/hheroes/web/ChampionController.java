package mav.com.hheroes.web;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.Currency;
import mav.com.hheroes.services.ChampionService;
import mav.com.hheroes.services.GameService;
import mav.com.hheroes.services.dtos.ChampionDataDTO;
import mav.com.hheroes.services.dtos.response.ChampionResponseDTO;

@RestController
@RequestMapping("/champion")
public class ChampionController {
	@Resource
	private ChampionService championService;
	
	@Resource
	private HttpSession httpSession;
	
	@PostMapping("/{id}/fight")
	public ChampionResponseDTO fightChampion(@PathVariable("id") Integer id, @RequestParam("currency") Currency currency) throws IOException {
		return championService.fightChampion(id, currency, httpSession.getAttribute(GameService.LOGIN).toString());
	}
	
	@GetMapping("/all")
	public List<ChampionDataDTO> getAllChampion() throws IOException {
		return championService.getAllChampions(httpSession.getAttribute(GameService.LOGIN).toString());
	}
}
