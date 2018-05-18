package mav.com.hheroes.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import mav.com.hheroes.domain.Boss;
import mav.com.hheroes.domain.dao.BossRepository;
import mav.com.hheroes.services.dtos.ResponseDTO;
import mav.com.hheroes.services.exceptions.ObjectNotFoundException;
import mav.com.hheroes.services.mappers.DomainMapper;

@Service
public class BossService {
	private final Logger logger = Logger.getLogger(getClass());

	@Resource
	private GameService gameService;

	@Resource
	private BossRepository bossRepository;

	public BossService(GameService gameService) {
		this.gameService = gameService;
	}

	public List<Boss> getAllBosses() {
		return StreamSupport.stream(bossRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	public Boss getBoss(Integer id) throws ObjectNotFoundException {
		Optional<Boss> boss = bossRepository.findById(id);

		if (!boss.isPresent()) {
			throw new ObjectNotFoundException(String.format("Le boss d'ID %s n'existe pas", id));
		}

		return boss.get();
	}

	public ResponseDTO fight(Boss boss, String login) throws IOException {
		return gameService.fightBoss(DomainMapper.asBossDTO(boss), login);
	}

	public ResponseDTO fight(Integer id, String login) throws IOException, ObjectNotFoundException {
		return fight(getBoss(id), login);
	}

	public List<String> destroy(Integer id, String login, boolean log) throws IOException, ObjectNotFoundException {
		List<String> rewards = new ArrayList<>();
		Boss boss = getBoss(id);

		ResponseDTO response = fight(boss, login);
		while (response.getSuccess()) {
			rewards.add(response.getReward().getDrops());
			if (log) {
				logger.info(String.format("\tCollected from %s : %s", boss.getLibelle(), response.getReward()));
			}
			response = fight(boss, login);
		}

		return rewards;
	}

	public List<String> destroy(Integer id, String login) throws IOException, ObjectNotFoundException {
		return destroy(id, login, false);
	}

	public void setGameService(GameService gameService) {
		this.gameService = gameService;
	}
}
