package mav.com.hheroes.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import mav.com.hheroes.services.dtos.JoueurDTO;
import mav.com.hheroes.services.dtos.response.ResponseDTO;

@Service
public class ArenaService {
	private static final int NB_ARENA = 3;
	@Autowired
	private GameService gameService;
	
	public ArenaService(GameService gameService) {
		this.gameService = gameService;
	}
	
	public Optional<JoueurDTO> getJoueur(int numArena, String login) throws IOException {
		Document arena = gameService.getBattleForArena(numArena, login);
		return getJoueur(arena);	
	}
	
	public Optional<JoueurDTO> getJoueur(int league, long idJoueur, String login) throws IOException {
		Document arena = gameService.getBattleForLeague(league, idJoueur, login);
		return getJoueur(arena);
	}

	private Optional<JoueurDTO> getJoueur(Document arena) throws IOException, JsonParseException, JsonMappingException {
		String joueurJson = "";
		List<JoueurDTO> joueurs = new ArrayList<>();
		
		Elements select = arena.select("script");
		for (Element script : select) {
			if (script.html().contains("hh_battle_players")) {
				joueurJson = script.html().replaceAll("(?s).*?hh_battle_players = (\\[.*?\\]);.+", "$1");
			}
		}
		
		if (!joueurJson.isEmpty()) {
			joueurs = new ObjectMapper().readValue(joueurJson, new TypeReference<List<JoueurDTO>>(){});
		}
		return (joueurs.size() > 1) ? Optional.of(joueurs.get(1)) : Optional.empty();
	}
	
	public ResponseDTO fight(JoueurDTO joueur, String login) throws IOException {
		return gameService.fightJoueur(joueur, login);
	}
	
	public List<JoueurDTO> getAllJoueurs(String login) throws IOException {
		List<JoueurDTO> joueurs = new ArrayList<>();
		for (int i = 0; i < NB_ARENA; i++) {
			getJoueur(i, login).ifPresent(joueurs::add);
		}
		return joueurs;
	}
}
