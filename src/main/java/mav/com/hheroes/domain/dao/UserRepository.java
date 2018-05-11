package mav.com.hheroes.domain.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import mav.com.hheroes.domain.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	Optional<User> findByEmailIgnoreCase(String email);
}
