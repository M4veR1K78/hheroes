package mav.com.hheroes.services;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import mav.com.hheroes.domain.User;
import mav.com.hheroes.domain.dao.UserRepository;

@Service
public class UserService {
	private static final String ALGO = "AES";

	@Value("${security.key}")
	private String securityKey;

	@Autowired
	private UserRepository userRepository;

	public User create(User user) {
		user.setEmail(user.getEmail().toLowerCase());
		user.setPassword(encrypt(user.getPassword(), securityKey));
		return userRepository.save(user);
	}

	public Optional<User> getByEmail(String email) {
		Optional<User> user = userRepository.findByEmailIgnoreCase(email);
		user.ifPresent(decryptPassword());
		return user;
	}

	public void update(User userToUpdate) {
		userRepository.findById(userToUpdate.getId()).ifPresent(user -> {
			user.setPassword(encrypt(userToUpdate.getPassword(), securityKey));
			user.setAutoSalary(userToUpdate.isAutoSalary());
			user.setEmail(userToUpdate.getEmail());
			user.setBoss(userToUpdate.getBoss());
			user.setAdmin(userToUpdate.isAdmin());
			userRepository.save(user);
		});
	}
	
	public List<User> getWithAutoSalary() {
		return userRepository.findByAutoSalaryIsTrue().stream()
				.peek(decryptPassword())
				.collect(Collectors.toList());
	}

	private Consumer<? super User> decryptPassword() {
		return user -> {
			if (user.getPassword() != null) {
				user.setPassword(decrypt(user.getPassword(), securityKey));
			}
		};
	}

	public String encrypt(String strClearText, String strKey) {
		String strData = "";

		try {
			SecretKeySpec skeyspec = new SecretKeySpec(strKey.getBytes(), ALGO);
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
			byte[] encrypted = cipher.doFinal(strClearText.getBytes("UTF-8"));
			strData = new String(Base64.getEncoder().encode(encrypted));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;
	}

	public String decrypt(String strEncrypted, String strKey) {
		String strData = "";

		try {
			SecretKeySpec skeyspec = new SecretKeySpec(strKey.getBytes(), ALGO);
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.DECRYPT_MODE, skeyspec);
			byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(strEncrypted));
			strData = new String(decrypted, "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;
	}
}
