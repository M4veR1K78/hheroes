package mav.com.hheroes.web;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.Fille;
import mav.com.hheroes.services.FilleService;
import mav.com.hheroes.services.dtos.SalaryDTO;

@RestController
@RequestMapping("/filles")
public class FilleController
{
	@Autowired
	private FilleService filleService;
		
	@RequestMapping(method = RequestMethod.GET)
	public List<Fille> getAllFilles() throws IOException {
		return filleService.getFilles();
	}
	
	@RequestMapping(value = "/{id}/avatar/{grade}", method = RequestMethod.GET, produces = "image/png")
	public ResponseEntity<byte[]> getAvatar(@PathVariable Integer id, @PathVariable Integer grade) throws IOException {
		byte[] image = filleService.getAvatarImage(id, grade);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
	}
	
	@RequestMapping(value = "/{id}/salary", method = RequestMethod.GET)
	public SalaryDTO getSalary(@PathVariable Integer id) throws IOException {
		return filleService.collectSalary(id);
	}
	
	@RequestMapping(value = "/collectAll", method = RequestMethod.POST)
	public void collectSalary() throws IOException {
		List<Fille> filles = filleService.getFilles();
		for (Fille fille : filles) {
			if (fille.isCollectable()) {
				getSalary(fille.getId());
			}
		}
	}
}
