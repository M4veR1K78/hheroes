package mav.com.hheroes.services;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.IntStream;

import javax.annotation.Resource;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import mav.com.hheroes.domain.Hero;
import mav.com.hheroes.services.dtos.HeroDTO;
import mav.com.hheroes.services.dtos.StatUpdateResponseDTO;
import mav.com.hheroes.services.mappers.DomainMapper;

@Service
public class HeroService {
	@Resource
	private GameService gameService;

	public Hero getHero(String login) throws IOException {
		Document home = gameService.getHome(login);
		String heroJson = "";

		Elements select = home.select("script");
		for (Element script : select) {
			if (script.html().contains("Hero.infos")) {
				heroJson = script.html().replaceAll("(?s).*?(\\{.*?\\});.+", "$1");
			}
		}

		Hero hero = DomainMapper.asHero(new ObjectMapper().readValue(heroJson, HeroDTO.class));
		hero.setAvatarUrl(home.select("div[rel=\"avatar\"] img").attr("src"));
		return hero;
	}

	public StatUpdateResponseDTO updateStats(int carac, int size, String login) {
		return IntStream.range(0, size).mapToObj(i -> {
			try {
				return gameService.updateStat(carac, login);
			} catch (IOException e) {
				return null;
			}
		})
				.filter(Objects::nonNull)
				.filter(StatUpdateResponseDTO::isSuccess)
				.reduce((f, s) -> s).orElse(null);
	}
}
