package mav.com.hheroes.domain.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import mav.com.hheroes.domain.Boss;

public interface BossRepository extends CrudRepository<Boss, Integer> {
	Optional<Boss> findById(Integer id);
}
