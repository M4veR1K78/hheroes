package mav.com.hheroes.web;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.Boss;
import mav.com.hheroes.services.BossService;
import mav.com.hheroes.services.dtos.ResponseDTO;
import mav.com.hheroes.services.exceptions.ObjectNotFoundException;

@RestController
@RequestMapping("/boss")
public class BossController {
	@Resource
	private BossService bossService;

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<Boss> getBosses() {
		return bossService.getAllBosses();
	}

	@RequestMapping(value = "/{id}/fight", method = RequestMethod.POST)
	public ResponseDTO fight(@PathVariable String bossId) throws IOException, ObjectNotFoundException {
		return bossService.fight(bossId);
	}

	@RequestMapping(value = "/{id}/destroy", method = RequestMethod.POST)
	public List<String> destroyBoss(@PathVariable("id") String bossId) throws IOException, ObjectNotFoundException {
		return bossService.destroy(bossId);
	}
}
