package mav.com.hheroes.services;

import java.io.IOException;
import java.util.Objects;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import mav.com.hheroes.domain.Boss;
import mav.com.hheroes.domain.Mission;
import mav.com.hheroes.services.dtos.ResponseDTO;
import mav.com.hheroes.services.dtos.SalaryDTO;
import mav.com.hheroes.services.exceptions.AuthenticationException;

/**
 * Ce service s'occupe de faire les différentes requêtes sur le jeu HHeroes et
 * d'en récupérer les données.
 *
 */
@Service
public class GameService {
	private static final String URL_HHEROES = "https://www.hentaiheroes.com";
	private static final String URL_HOME = URL_HHEROES + "/home.html";
	private static final String URL_HAREM = URL_HHEROES + "/harem/1";
	private static final String URL_SHOP = URL_HHEROES + "/shop.html";
	private static final String URL_MISSIONS = URL_HHEROES + "/activities.html?tab=missions";
	private static final String URL_LOGIN = URL_HHEROES + "/phoenix-ajax.php";
	private static final String URL_ACTION = URL_HHEROES + "/ajax.php";
	public static final String COOKIE_NAME = "stay_online";

	private String cookie;

	public String login(String mail, String password) throws AuthenticationException {
		Response res;
		try {
			res = Jsoup.connect(URL_LOGIN)
					.data("login", mail)
					.data("password", password)
					.data("stay_online", "1")
					.data("module", "Member")
					.data("action", "form_log_in")
					.data("call", "Member")
					.timeout(0)
					.ignoreContentType(true)
					.method(Method.POST)
					.execute();
		} catch (IOException e) {
			throw new AuthenticationException("L'authentification a échoué", e);
		}

		if (StringUtils.isEmpty(res.cookie(COOKIE_NAME))) {
			throw new AuthenticationException("Les identifiants n'ont pas réussi à authentifier l'utilisateur");
		}

		setCookie(res.cookie(COOKIE_NAME));
		return res.cookie(COOKIE_NAME);
	}

	public Document getHarem() throws IOException {
		return getPage(URL_HAREM);
	}

	public Document getShop() throws IOException {
		return getPage(URL_SHOP);
	}

	public Document getMissions() throws IOException {
		return getPage(URL_MISSIONS);
	}
	
	public Document getHome() throws IOException {
		return getPage(URL_HOME);
	}

	private Document getPage(String url) throws IOException {
		return Jsoup.connect(url)
				.cookie(COOKIE_NAME, getCookie())
				.parser(Parser.htmlParser())
				.get();
	}

	public byte[] getGirlImage(String url) throws IOException {
		Response resultImageResponse = Jsoup.connect(URL_HHEROES + url)
				.cookie(COOKIE_NAME, getCookie())
				.ignoreContentType(true).execute();

		return resultImageResponse.bodyAsBytes();
	}

	public String getCookie() {
		return cookie;
	}

	public byte[] getAvatar(Integer girlId, Integer grade) throws IOException {
		Response resultImageResponse = Jsoup
				.connect(String.format("%s/img/girls/%s/ava%s.png", URL_HHEROES, girlId, grade))
				.cookie(COOKIE_NAME, getCookie())
				.ignoreContentType(true)
				.execute();

		return resultImageResponse.bodyAsBytes();
	}

	public byte[] getImage(String urlImage) throws IOException {
		Response resultImageResponse = Jsoup
				.connect(urlImage)
				.cookie(COOKIE_NAME, getCookie())
				.ignoreContentType(true)
				.execute();

		return resultImageResponse.bodyAsBytes();
	}

	/**
	 * Réponse au format <code> {"time":4320,"money":2760,"success":true}</code>
	 * 
	 * @param girlId
	 * @return
	 * @throws IOException
	 */
	public SalaryDTO getSalary(Integer girlId) throws IOException {
		Response result = Jsoup.connect(URL_ACTION)
				.cookie(COOKIE_NAME, getCookie())
				.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.data("class", "Girl")
				.data("action", "get_salary")
				.data("who", girlId.toString())
				.method(Method.POST)
				.ignoreContentType(true)
				.execute();

		if (result.statusCode() == HttpStatus.OK.value()) {
			return new ObjectMapper().readValue(result.body(), SalaryDTO.class);
		}

		return null;
	}

	public boolean isConnected() {
		return cookie != null;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public void acceptMission(Mission mission) throws IOException {
		Jsoup.connect(URL_ACTION)
				.cookie(COOKIE_NAME, getCookie())
				.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.data("class", "Missions")
				.data("action", "start_mission")
				.data("id_mission", mission.getId())
				.data("id_member_mission", mission.getIdMember())
				.method(Method.POST)
				.ignoreContentType(true)
				.execute();
	}

	public ResponseDTO fightBoss(Boss boss) throws IOException {
		Objects.requireNonNull(boss, "Le boss ne doit pas être null");
		
		Response res = Jsoup.connect(URL_ACTION)
				.cookie(COOKIE_NAME, getCookie())
				.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.data("class", "Battle")
				.data("action", "fight")
				.data("who[id_troll]", boss.getId())
				.data("who[orgasm]", String.valueOf(boss.getOrgasm()))
				.data("who[ego]", String.valueOf(boss.getEgo()))
				.data("who[x]", String.valueOf(boss.getX().intValue()))
				.data("who[d]", String.valueOf(boss.getD()))
				.data("who[nb_org]", String.valueOf(boss.getNbOrg()))
				.data("who[figure]", String.valueOf(boss.getFigure()))
				.data("who[id_world]", boss.getWorld())
				.method(Method.POST)
				.ignoreContentType(true)
				.execute();

		return new ObjectMapper().readValue(res.body(), ResponseDTO.class);
	}
}
