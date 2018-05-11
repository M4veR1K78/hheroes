package mav.com.hheroes.services;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import mav.com.hheroes.domain.User;
import mav.com.hheroes.domain.dao.UserRepository;

@Service
public class UserService {
	@Resource
	private UserRepository userRepository;
	
	public User create(User user) {
		user.setEmail(user.getEmail().toLowerCase());
		return userRepository.save(user);
	}
	
	public Optional<User> getByEmail(String email) {
		return userRepository.findByEmailIgnoreCase(email);
	}
	
	public void update(User user) {
		user.setEmail(user.getEmail().toLowerCase());
		userRepository.save(user);
	}
}
