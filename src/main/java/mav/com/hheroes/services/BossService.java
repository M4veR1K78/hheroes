package mav.com.hheroes.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import mav.com.hheroes.domain.Boss;
import mav.com.hheroes.services.dtos.ResponseDTO;
import mav.com.hheroes.services.exceptions.ObjectNotFoundException;
import mav.com.hheroes.services.mappers.DomainMapper;

@Service
public class BossService {
	@Resource
	private GameService gameService;
	
	private List<Boss> bosses = new ArrayList<>();
		
	public BossService(GameService gameService) {
		this.gameService= gameService;
	}

	public List<Boss> getAllBosses() {
		if (bosses.isEmpty()) {
			bosses.add(create(Boss.DARK_LORD, "Dark lord", 2394, 946.82, Double.valueOf(0), 946.82, 0, 3, "2"));
			bosses.add(create(Boss.ESPION_NINJA, "Espion Ninja", 5618, 2076.32, Double.valueOf(0), 2076.32, 0, 12, "3"));
		}
		return bosses;
	}

	private Boss create(String id, String libelle, Integer orgasm, Double ego, Double x, Double d, Integer nbOrg,
			Integer figure, String world) {
		Boss boss = new Boss();
		boss.setLibelle(libelle);
		boss.setId(id);
		boss.setOrgasm(orgasm);
		boss.setEgo(ego);
		boss.setX(x);
		boss.setD(d);
		boss.setNbOrg(nbOrg);
		boss.setFigure(figure);
		boss.setWorld(world);
		return boss;
	}

	public Boss getBoss(String id) {
		return getAllBosses().stream()
				.filter(boss -> boss.getId().equals(id))
				.findFirst()
				.orElse(null);
	}
	
	public ResponseDTO fight(Boss boss) throws IOException {
		return gameService.fightBoss(DomainMapper.asBossDTO(boss));
	}
	
	public ResponseDTO fight(String id) throws IOException, ObjectNotFoundException {
		Boss boss = getBoss(id);
		
		if (boss == null) {
			throw new ObjectNotFoundException(String.format("Le boss d'ID %s n'existe pas", id));
		}
		
		return fight(boss);
	}
	
	public void destroy(String id) throws IOException, ObjectNotFoundException {
		Boss boss = getBoss(id);
		
		if (boss == null) {
			throw new ObjectNotFoundException(String.format("Le boss d'ID %s n'existe pas", id));
		}
		
		ResponseDTO response = fight(boss);
		while (response.getSuccess()) {
			response = fight(boss);
		}
	}
}
