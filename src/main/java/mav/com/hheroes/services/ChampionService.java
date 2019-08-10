package mav.com.hheroes.services;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import mav.com.hheroes.domain.Currency;
import mav.com.hheroes.services.dtos.champion.ChampionDataDTO;
import mav.com.hheroes.services.dtos.champion.responses.ChampionResponseDTO;
import mav.com.hheroes.services.dtos.response.ResponseDTO;

@Service
public class ChampionService {
	@Autowired
	private GameService gameService;

	private List<Integer> championIds = Arrays.asList(1, 2, 3, 4, 5, 6);

	public ChampionService(GameService gameService) {
		this.gameService = gameService;
	}

	public ChampionResponseDTO fightChampion(Integer championId, Currency currency, String login) throws IOException {
		ChampionDataDTO champion = getChampion(championId, login);
		return gameService.fightChampion(champion, currency, login);
	}

	public ChampionDataDTO getChampion(Integer championId, String login) {
		Document champions;
		try {
			champions = gameService.getChampionArena(championId, login);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

		return champions.select("script").stream().map(script -> script.html())
				.filter(html -> html.contains("var championData = ")).findFirst()
				.map(line -> line.replaceAll("^.* = (.+);\\n.+", "$1")).map(championJson -> {
					try {
						return new ObjectMapper().readValue(championJson, ChampionDataDTO.class);
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}).orElse(null);
	}

	public List<ChampionDataDTO> getAllChampions(String login) {
		List<ChampionDataDTO> liste = new ArrayList<>();
		championIds.stream()
				.map(championId -> getChampion(championId, login))
				.filter(Objects::nonNull)
				.forEach(liste::add);
		return liste;
	}

	public void setChampionIds(String[] championsIds) {
		this.championIds = Stream.of(championsIds).map(Integer::valueOf).collect(Collectors.toList());
	}

	public ResponseDTO reorderTeam(Integer championId, String login) throws IOException {
		ChampionDataDTO champion = getChampion(championId, login);
		return gameService.doChampionTeamReorder(champion, login);
	}
}
