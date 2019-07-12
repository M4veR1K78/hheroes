package mav.com.hheroes.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import mav.com.hheroes.domain.Currency;
import mav.com.hheroes.services.dtos.ChampionDataDTO;
import mav.com.hheroes.services.dtos.response.ChampionResponseDTO;

@Service
public class ChampionService {
	@Autowired
	private GameService gameService;

	private List<Integer> championIds = Arrays.asList(1, 2, 3, 4, 5, 6);
	
	public ChampionService(GameService gameService) {
		this.gameService = gameService;
	}

	public ChampionResponseDTO fightChampion(Integer championId, Currency currency, String login) throws IOException {
		return gameService.doChampionBattle(championId, currency, login);
	}

	public List<ChampionDataDTO> getAllChampions(String login) {
		List<ChampionDataDTO> liste = new ArrayList<>();
		championIds.forEach(championId -> {
			try {
				Document champions = gameService.getChampionArena(championId, login);
				champions.select("script").stream().map(script -> script.html())
					.filter(html -> html.contains("var championData = "))
					.findFirst()
					.map(line -> line.replaceAll("^.* = (.+);\\n.+", "$1"))
					.map(championJson -> {
						try {
							return new ObjectMapper().readValue(championJson, ChampionDataDTO.class);
						} catch (IOException e) {
							return null;
						}
					})
					.ifPresent(liste::add);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		});
		
		return liste;
	}
}
