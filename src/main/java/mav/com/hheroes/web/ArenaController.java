package mav.com.hheroes.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.services.ArenaService;
import mav.com.hheroes.services.dtos.JoueurDTO;
import mav.com.hheroes.services.dtos.ResponseDTO;
import mav.com.hheroes.services.exceptions.ObjectNotFoundException;

@RestController
@RequestMapping("/arena")
public class ArenaController {
	@Resource
	private ArenaService arenaService;
	
	@GetMapping("/{id}")
	public JoueurDTO getArena(@PathVariable Integer id) throws IOException {
		return arenaService.getJoueur(id).orElse(null);
	}
	
	@PostMapping("/{id}/fight")
	public ResponseDTO fight(@PathVariable Integer id) throws IOException, ObjectNotFoundException {
		return arenaService.fight(arenaService.getJoueur(id).orElse(null));
	}
	
	@GetMapping("/all")
	public List<JoueurDTO> getAllJoueurs() throws IOException {
		return arenaService.getAllJoueurs();
	}
	
	@PostMapping("/all/fight")
	public List<ResponseDTO> fightAll() throws IOException, ObjectNotFoundException {
		List<ResponseDTO> fights = new ArrayList<>();
		List<JoueurDTO> joueurs = arenaService.getAllJoueurs();
		System.out.println(String.format("Fight all Start (%s players)", joueurs.size()));
		for (JoueurDTO joueur : joueurs) {
			String log = String.format("\tPlayer arena %s attacked.", joueur.getArena());
			ResponseDTO response = arenaService.fight(joueur);
			fights.add(response);
			if (response.getSuccess()) {
				log += String.format(" Results = %s", response.getReward().getWinner().equals(1) ? "Win" : "Loss");
			}
			System.out.println(log);
		}
		return fights;
	}
}
