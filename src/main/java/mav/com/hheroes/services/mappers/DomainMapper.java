package mav.com.hheroes.services.mappers;

import mav.com.hheroes.domain.Experience;
import mav.com.hheroes.domain.Hero;
import mav.com.hheroes.domain.Skill;
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
			hero.setEneryQuestMax(dto.getEneryQuestMax());
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
}
