package mav.com.hheroes.services;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import mav.com.hheroes.MainApplication;
import mav.com.hheroes.services.exceptions.AuthenticationException;

@SpringBootTest
@RunWith(SpringRunner.class)
@Profile({ "dev", "test" })
@Import(MainApplication.class)
public class TowerFameServiceTest {
	@Value("test.login")
	private String login;
	
	@Value("test.password")
	private String password;
	
	@Test
	@Ignore
	public void getOppenents() throws AuthenticationException {
//		gameService.login(login, password);
		System.out.println(login + " - " + password);
	}
}
