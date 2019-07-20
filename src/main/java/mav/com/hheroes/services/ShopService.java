package mav.com.hheroes.services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import mav.com.hheroes.domain.Cadeau;
import mav.com.hheroes.services.dtos.CadeauDTO;

@Service
public class ShopService {
	public List<Cadeau> getCadeauAvailable(Document shop) {
		shop.outputSettings().escapeMode(EscapeMode.base);
		shop.outputSettings().charset(Charset.defaultCharset());
		
		List<Cadeau> cadeaux = new ArrayList<>();
		
		Element giftShop = shop.select(".gift").get(0);
		giftShop.select(".slot").forEach(gift -> {		
			if (gift.hasText()) {
				try {
					CadeauDTO dto = new ObjectMapper().readValue(gift.attr("data-d"), CadeauDTO.class);
					Cadeau cadeau = new Cadeau();
					BeanUtils.copyProperties(dto, cadeau);
					cadeaux.add(cadeau);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		return cadeaux;
	}
}
