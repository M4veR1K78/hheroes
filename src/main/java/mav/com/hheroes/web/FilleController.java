package mav.com.hheroes.web;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.Fille;
import mav.com.hheroes.services.FilleService;
import mav.com.hheroes.services.GameService;
import mav.com.hheroes.services.dtos.BestGirlPerPositionDTO;
import mav.com.hheroes.services.dtos.SalaryDTO;

@RestController
@RequestMapping("/filles")
public class FilleController
{
	@Autowired
	private FilleService filleService;
		
	@Resource
	private HttpSession httpSession;
	
	@GetMapping
	public List<Fille> getAllFilles() throws IOException {
		return filleService.getFilles(httpSession.getAttribute(GameService.LOGIN).toString());
	}
	
	@RequestMapping(value = "/{id}/avatar/{grade}", method = RequestMethod.GET, produces = "image/png")
	public ResponseEntity<byte[]> getAvatar(@PathVariable Integer id, @PathVariable Integer grade) throws IOException {
		byte[] image = filleService.getAvatarImage(id, grade, httpSession.getAttribute(GameService.LOGIN).toString());
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
	}
	
	@GetMapping("/{id}/salary")
	public SalaryDTO getSalary(@PathVariable Integer id) throws IOException {
		return filleService.collectSalary(id, httpSession.getAttribute(GameService.LOGIN).toString());
	}
	
	@RequestMapping(value = "/collectAll", method = RequestMethod.POST)
	public void collectSalary() throws IOException {
		List<Fille> filles = filleService.getFilles(httpSession.getAttribute(GameService.LOGIN).toString());
		for (Fille fille : filles) {
			if (fille.isCollectable()) {
				getSalary(fille.getId());
			}
		}
	}
	
	@RequestMapping(value = "/doCollectAll", method = RequestMethod.POST)
	public void doCollectSalaries() throws IOException {
		filleService.doCollectAllSalaries(httpSession.getAttribute(GameService.LOGIN).toString());
	}
	
	@GetMapping("/bestPerPosition")
	public List<BestGirlPerPositionDTO> getBestFillesByPosition(@RequestParam int top) throws IOException {
		return filleService.getBestFillesByPosition(top, httpSession.getAttribute(GameService.LOGIN).toString());
	}
}
