package mav.com.hheroes.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.Currency;
import mav.com.hheroes.services.ChampionService;
import mav.com.hheroes.services.GameService;
import mav.com.hheroes.services.dtos.champion.ChampionDataDTO;
import mav.com.hheroes.services.dtos.champion.responses.ChampionResponseDTO;
import mav.com.hheroes.services.dtos.response.ResponseDTO;

@RestController
@RequestMapping("/champion")
public class ChampionController {
	@Autowired
	private ChampionService championService;
	
	@Autowired
	private HttpSession httpSession;
	
	@PostMapping("/{id}/fight")
	public ChampionResponseDTO fightChampion(@PathVariable("id") Integer id, @RequestParam("currency") Currency currency) throws IOException {
		return championService.fightChampion(id, currency, httpSession.getAttribute(GameService.LOGIN).toString());
	}
	
	@PostMapping("/{id}/reorder")
	public ResponseDTO reorderTeam(@PathVariable("id") Integer id) throws IOException {
		return championService.reorderTeam(id, httpSession.getAttribute(GameService.LOGIN).toString());
	}
	
	@GetMapping("/all")
	public List<ChampionDataDTO> getAllChampion() throws IOException {
		return championService.getAllChampions(httpSession.getAttribute(GameService.LOGIN).toString());
	}
}
