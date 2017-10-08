package mav.com.hheroes.services;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.springframework.stereotype.Service;

import mav.com.hheroes.domain.Cadeau;

@Service
public class ShopService {
	@Resource
	private GameService gameService;
	
	public List<Cadeau> getCadeauAvailable(Document shop) {
		shop.outputSettings().escapeMode(EscapeMode.base);
		shop.outputSettings().charset(Charset.defaultCharset());
		
		List<Cadeau> cadeaux = new ArrayList<>();
		
		Element giftShop = shop.select(".gift").get(0);
		giftShop.select(".slot").forEach(gift -> {
			Cadeau cadeau = new Cadeau();
			
			if (gift.hasText()) {
				cadeau.setId(Integer.valueOf(gift.attr("id_item")));
				cadeau.setUrlImage(gift.select("img").attr("src"));
				cadeau.setNom(gift.select(".item_tooltip h5").text());
				cadeau.setAffectation(Integer.valueOf(gift.select(".item_tooltip div[carac]").text().replaceAll(".*?(\\d+).*", "$1")));
				cadeau.setPrix(Integer.valueOf(gift.select(".item_tooltip div[rel]")
						.text()
						.replaceAll("\u00a0", "")
						.replace(",", "")));
				cadeaux.add(cadeau);
			}
		});
		
		return cadeaux;
	}
}
