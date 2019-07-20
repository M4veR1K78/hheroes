package mav.com.hheroes.services;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mav.com.hheroes.domain.Opponent;

@Service
public class TowerFameService {
	@Autowired
	private GameService gameService;
	
	public TowerFameService(GameService gameService) {
		this.gameService = gameService;
	}
	
	public List<Opponent> getOpponents(String login) throws IOException {
		Document tower = gameService.getTowerOfFame(login);

		return tower.select(".leagues_table .leadTable tr").stream()
				.map(tr -> {
					Opponent opponent = new Opponent();
					opponent.setId(Long.valueOf(tr.attr("sorting_id").trim()));
					opponent.setRank(Integer.valueOf(tr.select("td:nth-child(1)").text().trim()));
					opponent.setName(tr.select("td:nth-child(2)").text().trim());
					opponent.setLevel(Integer.valueOf(tr.select("td:nth-child(3)").text().trim()));
					String temp[] = tr.select("td:nth-child(4)").text().trim().split("/");
					if (!temp[0].equals("-")) {
						opponent.setNbAttack(Integer.valueOf(temp[0]));						
					}
					opponent.setPoints(Integer.valueOf(tr.select("td:nth-child(5)").text().trim().replaceAll("\u00a0", "")));
					return opponent;
				})
				.filter(op -> op.getNbAttack() != null && op.getNbAttack() < 3)
				.sorted(Comparator.comparing(Opponent::getRank, Comparator.reverseOrder()))
				.collect(Collectors.toList());
	}
}
