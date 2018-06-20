package mav.com.hheroes.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import mav.com.hheroes.domain.Fille;
import mav.com.hheroes.domain.Skill;
import mav.com.hheroes.services.dtos.FilleDTO;
import mav.com.hheroes.services.dtos.SalaryDTO;

@Service
public class FilleService {
	private final Logger logger = Logger.getLogger(getClass());

	private static final Double COEFF_1_STAR = 1.3;
	private static final Double COEFF_2_STAR = 1.6;
	private static final Double COEFF_3_STAR = 1.9;
	private static final Double COEFF_4_STAR = 2.2;
	private static final Double COEFF_5_STAR = 2.5;

	@Resource
	private GameService gameService;

	public static boolean autoCollect;

	public FilleService(GameService gameService) {
		this.gameService = gameService;
	}

	public List<Fille> getFilles(String login) throws IOException {
		Document harem = gameService.getHarem(login);

		harem.outputSettings().escapeMode(EscapeMode.base);
		harem.outputSettings().charset(Charset.defaultCharset());

		List<Fille> filles = new ArrayList<>();

		Elements leftPanel = harem.select("#harem_left div[girl]");
		Elements rightPanel = harem.select("#harem_right div[girl]");

		rightPanel.forEach(girl -> {
			Fille fille = new Fille();
			fille.setId(Integer.valueOf(girl.select("div[girl]").attr("girl")));
			fille.setName(girl.select("h3").text());
			if (fille.getName().isEmpty()) {
				return;
			}

			// récupération de données de la liste de gauche
			Elements leftListInfo = leftPanel.select("[girl=" + fille.getId() + "]");
			fille.setSalary(
					Double.valueOf(cleanDoubleString(leftListInfo.select(".collect_money .s_value").text())));
			fille.setCollectable(leftListInfo.select(".salary.loads>button").text().trim().isEmpty());
			String avatarUrl = leftListInfo.select(".left img").attr("src");

			fille.setAvatar(avatarUrl);

			// retour à la liste de droite
			Element typeElement = girl.select("h3 span[carac]").get(0);
			Skill typeFille = Skill.valueOfClass(typeElement.attr("carac"));
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
			}

			fille.setAffLeftNextLevel(affLeft);
			fille.setCumulAff(cumulAff.replaceAll("\u00a0", " ").replace(",", " "));

			fille.setSalaryPerHour(Double
					.valueOf(cleanDoubleString(girl.select(".girl_line .revenue-title .salary").text().replace("/h", ""))));
			fille.setFavoritePosition(girl.select(".girl_pos img").attr("src"));

			Elements assets = girl.select(".carac_girl div[carac]");
			fille.setHardcore(Double.valueOf(assets.get(0).text().replace(",", ".")));
			fille.setCharme(Double.valueOf(assets.get(1).text().replace(",", ".")));
			fille.setSavoirFaire(Double.valueOf(assets.get(2).text().replace(",", ".")));
			fille.setExpertiseBaseValue(getInitialExpertiseValue(fille));

			filles.add(fille);
		});

		Elements select = harem.select("script");
		List<FilleDTO> dtos = new ArrayList<>();
		for (Element script : select) {
			if (script.html().contains("var girlsDataList = {}")) {
				String girls = script.html()
						.substring(script.html().indexOf("var girlsDataList = {}"),
								script.html().indexOf("var girls = {};"));
				
				Arrays.asList(girls.split("\\n")).stream()
						.filter(line -> line.contains("girlsDataList"))
						.forEach(line -> {
							String filleJson = line.replaceAll("(?s).*girlsDataList\\['\\d+'\\] = (.+?);.*", "$1");
							try {
								dtos.add(new ObjectMapper().readValue(filleJson, FilleDTO.class));
							} catch (IOException e) {
								// nothing to do
							}
						});
			}
		}
		
		filles.stream().forEach(fille -> {
			dtos.stream().filter(dto -> dto.getIdGirl().equals(fille.getId())).findFirst().ifPresent(dto -> {
				fille.setPayTime(dto.getPayTime());
				fille.setPayIn(dto.getPayIn());
				fille.setPseudo(dto.getName());
			});
		});

		rankGirls(filles);
		return filles;
	}

	public SalaryDTO collectSalary(Integer filleId, String login) throws IOException {
		return gameService.getSalary(filleId, login);
	}

	public byte[] getAvatarImage(Integer filleId, Integer grade, String login) throws IOException {
		return gameService.getAvatar(filleId, grade, login);
	}

	public double collectAllSalaries(String login) throws IOException {
		return getFilles(login).stream()
				.filter(Fille::isCollectable)
				.peek(fille -> {
					try {
						collectSalary(fille.getId(), login);
					} catch (IOException e) {
						logger.error(e);
					}
				})
				.mapToDouble(Fille::getSalary)
				.sum();
	}

	private String cleanExpAffLeft(String value) {
		return value.replaceAll("\u00a0", " ")
				.replaceAll(".*?(\\d+[ ,]?\\d*)", "$1")
				.replaceAll(",", " ");
	}

	/**
	 * Pour les string destinés à être transformés en double.
	 * 
	 * @param value
	 * @return
	 */
	private String cleanDoubleString(String value) {
		return value.replace("\u00a0", "").replace(",", "");
	}

	private void rankGirls(List<Fille> filles) {
		filles.stream().collect(Collectors.groupingBy(Fille::getTypeId))
				.entrySet()
				.forEach(entry -> {
					rank(entry.getValue().stream(), Fille::getExpertiseBaseValue, Comparator.reverseOrder())
							.entrySet().forEach(e -> {
								e.getValue().forEach(fille -> fille.setExpertiseRanking(e.getKey()));
							});
				});

	}

	static <T, V> SortedMap<Integer, List<T>> rank(Stream<T> stream, Function<T, V> propertyExtractor,
			Comparator<V> propertyComparator) {
		return stream.sorted(Comparator.comparing(propertyExtractor, propertyComparator))
				.collect(TreeMap::new,
						(rank, item) -> {
							V property = propertyExtractor.apply(item);
							if (rank.isEmpty()) {
								rank.put(new Integer(1), new LinkedList<T>());
							} else {
								Integer r = rank.lastKey();
								List<T> items = rank.get(r);
								if (!property.equals(propertyExtractor.apply(items.get(0)))) {
									rank.put(r + items.size(), new LinkedList<T>());
								}
							}
							rank.get(rank.lastKey()).add(item);
						},
						(rank1, rank2) -> {
							int lastRanking = rank1.lastKey();
							int offset = lastRanking + rank1.get(lastRanking).size() - 1;
							if (propertyExtractor.apply(rank1.get(lastRanking).get(0)) == propertyExtractor
									.apply(rank2.get(rank2.firstKey()).get(0))) {
								rank1.get(lastRanking).addAll(rank2.get(rank2.firstKey()));
								rank2.remove(rank2.firstKey());
							}
							rank2.forEach((r, items) -> {
								rank1.put(offset + r, items);
							});
						});
	}

	/**
	 * Récupère la valeur initiale dans le domaine d'expertise de la fille.
	 * 
	 * @param fille
	 * @return
	 */
	private Double getInitialExpertiseValue(Fille fille) {
		Double value = 0.0;
		switch (fille.getTypeId()) {
		case 1:
			value = fille.getHardcore();
			break;
		case 2:
			value = fille.getCharme();
			break;
		case 3:
			value = fille.getSavoirFaire();
			break;
		}
		value = value / fille.getLevel();

		switch (fille.getGrade()) {
		case 1:
			value = value / COEFF_1_STAR;
			break;
		case 2:
			value = value / COEFF_2_STAR;
			break;
		case 3:
			value = value / COEFF_3_STAR;
			break;
		case 4:
			value = value / COEFF_4_STAR;
			break;
		case 5:
			value = value / COEFF_5_STAR;
			break;
		}
		return new BigDecimal(value)
				.setScale(2, RoundingMode.HALF_UP)
				.doubleValue();
	}

	/**
	 * Collecte les salaires à l'infini.
	 * 
	 * @throws IOException
	 */
	@Async
	public void doCollectAllSalaries(String login) throws IOException {
		if (!autoCollect) {
			autoCollect = true;

			while (autoCollect) {
				collectAllSalaries(login);
				try {
					// toutes les 15 minutes
					Thread.sleep(15 * 60 * 1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public GameService getGameService() {
		return gameService;
	}
}
