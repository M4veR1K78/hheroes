package mav.com.hheroes.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.services.ArenaService;
import mav.com.hheroes.services.GameService;
import mav.com.hheroes.services.dtos.JoueurDTO;
import mav.com.hheroes.services.dtos.response.ResponseDTO;
import mav.com.hheroes.services.exceptions.ObjectNotFoundException;

@RestController
@RequestMapping("/arena")
public class ArenaController {
	@Autowired
	private ArenaService arenaService;

	@Autowired
	private HttpSession httpSession;

	@GetMapping("/{id}")
	public JoueurDTO getArena(@PathVariable Integer id) throws IOException {
		return arenaService.getJoueur(id, httpSession.getAttribute(GameService.LOGIN).toString()).orElse(null);
	}

	@PostMapping("/{id}/fight")
	public ResponseDTO fight(@PathVariable Integer id) throws IOException, ObjectNotFoundException {
		return arenaService.fight(
				arenaService.getJoueur(id, httpSession.getAttribute(GameService.LOGIN).toString()).orElse(null),
				httpSession.getAttribute(GameService.LOGIN).toString());
	}

	@GetMapping("/all")
	public List<JoueurDTO> getAllJoueurs() throws IOException {
		return arenaService.getAllJoueurs(httpSession.getAttribute(GameService.LOGIN).toString());
	}

	@PostMapping("/all/fight")
	public List<ResponseDTO> fightAll() throws IOException, ObjectNotFoundException {
		List<ResponseDTO> fights = new ArrayList<>();
		List<JoueurDTO> joueurs = arenaService.getAllJoueurs(httpSession.getAttribute(GameService.LOGIN).toString());
		System.out.println(String.format("Fight all Start (%s players)", joueurs.size()));
		for (JoueurDTO joueur : joueurs) {
			String log = String.format("\tPlayer arena %s attacked.", joueur.getArena());
			ResponseDTO response = arenaService.fight(joueur, httpSession.getAttribute(GameService.LOGIN).toString());
			fights.add(response);
			if (response.getSuccess()) {
				log += String.format(" Results = %s", response.getEnd().getRewards().isLose() ? "Loss" : "Win");
			}
			System.out.println(log);
		}
		return fights;
	}
}
