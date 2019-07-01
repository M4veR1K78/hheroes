package mav.com.hheroes.web;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.Currency;
import mav.com.hheroes.services.ChampionService;
import mav.com.hheroes.services.dtos.response.ChampionResponseDTO;

@RestController
@RequestMapping("/champion")
public class ChampionController {
	@Resource
	private ChampionService championService;
	
	@PostMapping("/fight")
	public ChampionResponseDTO fightChampion(@RequestParam("currency") Currency currency) throws IOException {
		return championService.fightChampion(currency, "");
	}
}
