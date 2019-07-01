package mav.com.hheroes.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mav.com.hheroes.domain.Currency;
import mav.com.hheroes.services.dtos.response.ChampionResponseDTO;

@Service
public class ChampionService {
	@Autowired
	private GameService gameService;
	
	public ChampionResponseDTO fightChampion(Currency currency, String login) throws IOException {
		return gameService.doChampionBattle(currency, login);
	}
}
