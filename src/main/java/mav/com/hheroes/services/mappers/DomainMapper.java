package mav.com.hheroes.services.mappers;

import mav.com.hheroes.domain.Boss;
import mav.com.hheroes.domain.Experience;
import mav.com.hheroes.domain.Fille;
import mav.com.hheroes.domain.Hero;
import mav.com.hheroes.domain.Rarity;
import mav.com.hheroes.domain.Skill;
import mav.com.hheroes.services.dtos.BossDTO;
import mav.com.hheroes.services.dtos.FilleDTO;
import mav.com.hheroes.services.dtos.FilleLightDTO;
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
			hero.setEneryChallenge(dto.getEnergyChallenge());
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

	public static Fille asFille(FilleDTO dto) {
		if (dto != null) {
			Fille fille = new Fille();
			fille.setId(dto.getIdGirl());
			fille.setName(dto.getRef().getFullName());
			fille.setPseudo(dto.getName());
			fille.setGrade(dto.getGraded());
			fille.setPayTime(dto.getPayTime());
			fille.setPayIn(dto.getPayIn());
			fille.setPseudo(dto.getName());
			fille.setSalary(dto.getSalary());
			fille.setSalaryPerHour(dto.getSalaryPerHour());
			fille.setAvatar(dto.getIcone());
			fille.setTypeId(dto.getTypeId());
			fille.setHardcore(dto.getCaracs().getHardcore());
			fille.setCharme(dto.getCaracs().getCharme());
			fille.setSavoirFaire(dto.getCaracs().getSavoirFaire());
			fille.setLevel(dto.getLevel());
			fille.setMaxAff(dto.getAffection().getMax());
			fille.setCurrentAff(dto.getAffection().getCurrent());
			fille.setAffLeftNextLevel(dto.getAffection().getLeft());
			fille.setExpLeftNextLevel(dto.getXp().getLeft());
			fille.setFavoritePosition(dto.getPosition());
			fille.setMaxed(dto.getAffection().isMaxed());
			fille.setUpgradable(dto.isUpgradable());
			fille.setRarity(Rarity.valueOfType(dto.getRarity()));
			return fille;
		}
		return null;
	}
	
	public static FilleLightDTO asFilleLightDTO(Fille fille) {
		if (fille == null) {
			return null;
		}
		
		FilleLightDTO dto = new FilleLightDTO();
		dto.setAvatar(fille.getAvatar());
		dto.setLevel(fille.getLevel());
		dto.setPseudo(fille.getPseudo());
		dto.setTypeId(fille.getTypeId());
		dto.setRarity(fille.getRarity());
		dto.setType(fille.getType());
		dto.setExpertiseBaseValue(fille.getExpertiseBaseValue());
		dto.setExpertiseRanking(fille.getExpertiseRanking());
		
		return dto;
	}
}
