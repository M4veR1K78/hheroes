package mav.com.hheroes.services.mappers;

import mav.com.hheroes.domain.Boss;
import mav.com.hheroes.domain.Experience;
import mav.com.hheroes.domain.Hero;
import mav.com.hheroes.domain.Skill;
import mav.com.hheroes.services.dtos.BossDTO;
import mav.com.hheroes.services.dtos.HeroDTO;

public class DomainMapper {
	public static Hero asHero(HeroDTO dto) {
		Hero hero = null;
		
		if (dto != null) {
			hero = new Hero();
			hero.setId(dto.getId());
			hero.setEnergyFight(dto.getEnergyFight());
			hero.setEnergyFightMax(dto.getEnergyFightMax());
			hero.setEnergyQuest(dto.getEnergyQuest());
			hero.setEnergyQuestMax(dto.getEneryQuestMax());
			hero.setKobans(dto.getKobans());
			hero.setLevel(dto.getLevel());
			hero.setName(dto.getName());
			hero.setXp(dto.getXp());
			hero.setMoney(dto.getMoney());
			hero.setClasse(Skill.valueOf(dto.getClasse()).getType());
			if (dto.getExperience() != null) {
				Experience exp = new Experience();
				exp.setCur(dto.getExperience().getCur());
				exp.setLeft(dto.getExperience().getLeft());
				exp.setLevel(dto.getExperience().getLevel());
				exp.setMin(dto.getExperience().getMin());
				exp.setMax(dto.getExperience().getMax());
				exp.setNextMax(dto.getExperience().getNextMax());
				hero.setExperience(exp);
			}
		}
		
		return hero;
	}
	
	public static BossDTO asBossDTO(Boss boss) {
		BossDTO dto = null;
		
		if (boss != null) {
			dto = new BossDTO();
			dto.setId(String.valueOf(boss.getId()));
			dto.setEgo(boss.getEgo());
			dto.setFigure(boss.getFigure());
			dto.setD(boss.getD());
			dto.setX(boss.getX());
			dto.setNbOrg(boss.getNbOrg());
			dto.setWorld(boss.getWorld());
			dto.setOrgasm(boss.getOrgasm());
		}
		
		return dto;
	}
}
