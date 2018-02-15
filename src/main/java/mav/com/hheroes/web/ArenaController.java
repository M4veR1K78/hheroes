package mav.com.hheroes.web;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public JoueurDTO getArena(@PathVariable Integer id) throws IOException {
		return arenaService.getJoueur(id).orElse(null);
	}
	
	@RequestMapping(value = "/{id}/fight", method = RequestMethod.POST)
	public ResponseDTO fight(@PathVariable Integer id) throws IOException, ObjectNotFoundException {
		return arenaService.fight(arenaService.getJoueur(id).orElse(null));
	}
}
