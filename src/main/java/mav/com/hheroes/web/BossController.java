package mav.com.hheroes.web;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.Boss;
import mav.com.hheroes.services.BossService;
import mav.com.hheroes.services.GameService;
import mav.com.hheroes.services.dtos.response.ResponseDTO;
import mav.com.hheroes.services.exceptions.ObjectNotFoundException;

@RestController
@RequestMapping("/boss")
public class BossController {
	@Resource
	private BossService bossService;
	
	@Resource
	private HttpSession httpSession;

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<Boss> getBosses() {
		return bossService.getAllBosses();
	}

	@RequestMapping(value = "/{id}/fight", method = RequestMethod.POST)
	public ResponseDTO fight(@PathVariable Integer bossId) throws IOException, ObjectNotFoundException {
		return bossService.fight(bossId, httpSession.getAttribute(GameService.LOGIN).toString());
	}

	@RequestMapping(value = "/{id}/destroy", method = RequestMethod.POST)
	public List<String> destroyBoss(@PathVariable("id") Integer bossId) throws IOException, ObjectNotFoundException {
		return bossService.destroy(bossId, httpSession.getAttribute(GameService.LOGIN).toString());
	}
}
