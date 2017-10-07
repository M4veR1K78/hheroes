package mav.com.hheroes.web;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.Mission;
import mav.com.hheroes.services.MissionService;

@RestController
@RequestMapping("/activity")
public class ActivityController {
	@Autowired
	private MissionService missionService;
	
	@RequestMapping(value = "/missions", method = RequestMethod.GET)
	public List<Mission> getMissions() throws IOException {
		return missionService.getMissions();
	}
}
