package mav.com.hheroes.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import mav.com.hheroes.services.dtos.JoueurDTO;
import mav.com.hheroes.services.dtos.ResponseDTO;

@Service
public class ArenaService {
	private static final int NB_ARENA = 3;
	@Resource
	private GameService gameService;
	
	public ArenaService(GameService gameService) {
		this.gameService = gameService;
	}
	
	public Optional<JoueurDTO> getJoueur(int numArena) throws IOException {
		Document arena = gameService.getBattle(numArena);
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
	
	public ResponseDTO fight(JoueurDTO joueur) throws IOException {
		return gameService.fightJoueur(joueur);
	}
	
	public List<JoueurDTO> getAllJoueurs() throws IOException {
		List<JoueurDTO> joueurs = new ArrayList<>();
		for (int i = 0; i < NB_ARENA; i++) {
			getJoueur(i).ifPresent(joueurs::add);
		}
		return joueurs;
	}
}
