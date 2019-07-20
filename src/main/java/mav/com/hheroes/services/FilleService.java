package mav.com.hheroes.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import mav.com.hheroes.domain.Fille;
import mav.com.hheroes.services.dtos.BestGirlPerPositionDTO;
import mav.com.hheroes.services.dtos.FilleDTO;
import mav.com.hheroes.services.dtos.SalaryDTO;
import mav.com.hheroes.services.mappers.DomainMapper;

@Service
public class FilleService {
	private static final String LINE_BREAK = "\\n";

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private GameService gameService;

	public static boolean autoCollect;

	public FilleService(GameService gameService) {
		this.gameService = gameService;
	}

	public List<Fille> getFilles(String login) throws IOException {
		final List<Fille> filles = new ArrayList<>();

		Document harem = gameService.getHarem(login);
		harem.select("script").stream().map(script -> script.html())
				.filter(html -> html.contains("var girlsDataList = {}")).findFirst()
				.map(html -> html.substring(html.indexOf("var girlsDataList = {}"), html.indexOf("var girls = {};")))
				.ifPresent(girls -> {
					Arrays.asList(girls.split(LINE_BREAK)).stream().filter(line -> line.contains("girlsDataList["))
							.map(this::serializeFille).filter(Objects::nonNull).filter(FilleDTO::isOwn)
							.map(DomainMapper::asFille).forEach(filles::add);
				});

		rankGirls(filles);
		return filles;
	}

	/**
	 * Parse la ligne et récupère le json. Le json est ensuite sérialisé en
	 * FilleDTO.
	 * 
	 * @param line ligne à parser.
	 * @return Un objet fille si l'objet json s'est bien parsé. {@code null} sinon.
	 */
	private FilleDTO serializeFille(String line) {
		String filleJson = line.replaceAll("(?s).*girlsDataList\\['\\d+'\\] = (.*);$", "$1");
		try {
			return new ObjectMapper().readValue(filleJson, FilleDTO.class);
		} catch (IOException e) {
			logger.error("Failed getting girl JSON", e);
			return null;
		}
	}

	public SalaryDTO collectSalary(Integer filleId, String login) throws IOException {
		return gameService.getSalary(filleId, login);
	}

	public byte[] getAvatarImage(Integer filleId, Integer grade, String login) throws IOException {
		return gameService.getAvatar(filleId, grade, login);
	}

	public double collectAllSalaries(String login) throws IOException {
		return getFilles(login).stream().filter(Fille::isCollectable).peek(fille -> {
			try {
				collectSalary(fille.getId(), login);
			} catch (IOException e) {
				logger.error(e);
			}
		}).mapToDouble(Fille::getSalary).sum();
	}

	private void rankGirls(List<Fille> filles) {
		filles.stream().collect(Collectors.groupingBy(Fille::getTypeId)).entrySet().forEach(entry -> {
			rank(entry.getValue().stream(), Fille::getExpertiseBaseValue, Comparator.reverseOrder()).entrySet()
					.forEach(e -> {
						e.getValue().forEach(fille -> fille.setExpertiseRanking(e.getKey()));
					});
		});

	}

	private static <T, V> SortedMap<Integer, List<T>> rank(Stream<T> stream, Function<T, V> propertyExtractor,
			Comparator<V> propertyComparator) {
		return stream.sorted(Comparator.comparing(propertyExtractor, propertyComparator)).collect(TreeMap::new,
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
				}, (rank1, rank2) -> {
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

	public List<BestGirlPerPositionDTO> getBestFillesByPosition(int top, String login) throws IOException {
		return getFilles(login).stream()
				.collect(Collectors.groupingBy(Fille::getFavoritePosition,
						Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
								.sorted(Comparator.comparing(Fille::getExpertiseBaseValue, Comparator.reverseOrder()))
								.limit(top).map(DomainMapper::asFilleLightDTO).collect(Collectors.toList()))))
				.entrySet().stream().map(entry -> {
					BestGirlPerPositionDTO dto = new BestGirlPerPositionDTO();
					dto.setPosition(entry.getKey());
					dto.setFilles(entry.getValue());
					return dto;
				})
				.sorted(Comparator.comparing(bgp -> bgp.getPosition().getNom()))
				.collect(Collectors.toList());
	}
}
