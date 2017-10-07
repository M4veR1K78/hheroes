package mav.com.hheroes.web;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.services.BossService;
import mav.com.hheroes.services.dtos.ResponseDTO;

@RestController
@RequestMapping("/boss")
public class BossController {
	@Resource
	private BossService bossService;	
	
	@RequestMapping(value = "/fight", method = RequestMethod.GET)
	public ResponseDTO fight(@RequestParam String bossId) throws IOException {
		return bossService.fight(bossService.getBoss(bossId));
	}
}
