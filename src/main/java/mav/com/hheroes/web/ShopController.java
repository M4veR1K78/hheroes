package mav.com.hheroes.web;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mav.com.hheroes.domain.Cadeau;
import mav.com.hheroes.services.GameService;
import mav.com.hheroes.services.ShopService;

@RestController
@RequestMapping("/shop")
public class ShopController {
	@Autowired
	private ShopService shopService;

	@Autowired
	private GameService heroesService;

	@RequestMapping(value = "/gifts", method = RequestMethod.GET)
	public List<Cadeau> getCadeaux() throws IOException {
		return shopService.getCadeauAvailable(heroesService.getShop());
	}
	
	@RequestMapping(value = "/gifts/image", method = RequestMethod.GET, produces = "image/png")
	public ResponseEntity<byte[]> getAvatar(@RequestParam("urlImage") String urlImage) throws IOException {
		byte[] image = heroesService.getGiftImage(urlImage);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
	}
}
