package mav.com.hheroes.services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import mav.com.hheroes.domain.Fille;
import mav.com.hheroes.domain.Skill;
import mav.com.hheroes.services.dtos.SalaryDTO;

@Service
public class FilleService {
	private final Logger logger = Logger.getLogger(getClass());
	
	@Resource
	private GameService gameService;

	public FilleService(GameService gameService) {
		this.gameService = gameService;
	}

	public List<Fille> getFilles() throws IOException {
		Document harem = gameService.getHarem();

		harem.outputSettings().escapeMode(EscapeMode.base);
		harem.outputSettings().charset(Charset.defaultCharset());

		List<Fille> filles = new ArrayList<>();

		Elements leftPanel = harem.select("#harem_left div[girl]");
		Elements rightPanel = harem.select("#harem_right div[girl]");

		rightPanel.forEach(girl -> {
			Fille fille = new Fille();
			fille.setId(Integer.valueOf(girl.select("div[girl]").attr("girl")));
			fille.setName(girl.select("h3").text());

			// récupération de données de la liste de gauche
			Elements leftListInfo = leftPanel.select("[girl=" + fille.getId() + "]");
			fille.setSalary(
					Double.valueOf(cleanDoubleString(leftListInfo.select(".blue_text_button .s_value").text())));
			fille.setCollectable(leftListInfo.select(".salary.loads>button").text().trim().isEmpty());
			String avatarUrl = leftListInfo.select(".left img").attr("src");

			try {
				fille.setAvatar(Base64.encodeBase64String(gameService.getGirlImage(avatarUrl)));
			} catch (IOException e) {
				logger.warn(String.format("Wasn't able to retrieve %s avatar. Url is : %s", fille.getName(), avatarUrl));
			}

			// retour à la liste de droite
			Element typeElement = girl.select("h3 span[carac]").get(0);
			Skill typeFille = Skill.valueOf(Integer.valueOf(typeElement.attr("carac")));
			fille.setType(typeFille.getType());
			fille.setTypeId(typeFille.getCode());
			fille.setLevel(Integer.parseInt(girl.select("span[rel='level']").text()));
			fille.setGrade(girl.select(".girl_quests .done").size());
			Elements subBlocks = girl.select(".girl_line");

			String expLeft = cleanExpAffLeft(subBlocks.get(0).select(".girl_exp_left").text());
			fille.setExpLeftNextLevel((!expLeft.isEmpty()) ? expLeft : Fille.LEVEL_MAX);
			String affLeft = cleanExpAffLeft(subBlocks.get(1).select(".girl_exp_left").text());
			String cumulAff = subBlocks.get(1).select(".girl_bar .over").text();
			if (affLeft.isEmpty()) {
				if (subBlocks.get(1).select(".grey_text_button").isEmpty()) {
					affLeft = cumulAff = Fille.LEVEL_UPGRADE;
				} else {
					affLeft = cumulAff = Fille.LEVEL_MAX;
				}
			} else {
			}

			fille.setAffLeftNextLevel(affLeft);
			fille.setCumulAff(cumulAff.replaceAll("\u00a0", " ").replace(",", " "));

			fille.setSalaryPerHour(Double
					.valueOf(girl.select(".girl_line .square .salary").text().replace("/h", "").replace(",", ".")));
			fille.setFavoritePosition(girl.select(".girl_pos span").text());

			Elements assets = girl.select(".square div[carac]");
			fille.setHardcore(Double.valueOf(assets.get(0).text().replace(",", ".")));
			fille.setCharme(Double.valueOf(assets.get(1).text().replace(",", ".")));
			fille.setSavoirFaire(Double.valueOf(assets.get(2).text().replace(",", ".")));

			filles.add(fille);
		});

		return filles;
	}

	public SalaryDTO collectSalary(Integer filleId) throws IOException {
		return gameService.getSalary(filleId);
	}

	public byte[] getAvatarImage(Integer filleId, Integer grade) throws IOException {
		return gameService.getAvatar(filleId, grade);
	}

	private String cleanExpAffLeft(String value) {
		return value.replaceAll("\u00a0", " ")
				.replaceAll(".*?(\\d+[ ,]?\\d*)", "$1")
				.replaceAll(",", " ");
	}

	/**
	 * Pour les string destiné à être transformé en double.
	 * 
	 * @param value
	 * @return
	 */
	private String cleanDoubleString(String value) {
		return value.replace("\u00a0", "").replace(",", "");
	}
}
