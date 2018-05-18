package mav.com.hheroes.web;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.Mission;
import mav.com.hheroes.services.GameService;
import mav.com.hheroes.services.MissionService;
import mav.com.hheroes.services.exceptions.AuthenticationException;

@RestController
@RequestMapping("/activity")
public class ActivityController {
	@Autowired
	private MissionService missionService;
	
	@Resource
	private HttpSession httpSession;
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<Mission> getMissions() throws IOException {
		return missionService.getMissions(httpSession.getAttribute(GameService.LOGIN).toString());
	}
	
	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public void startMissions() throws IOException, AuthenticationException {
		missionService.doAllMissions(httpSession.getAttribute(GameService.LOGIN).toString());
	}
}
