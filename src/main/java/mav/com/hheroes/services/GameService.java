package mav.com.hheroes.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

import mav.com.hheroes.domain.Mission;
import mav.com.hheroes.services.dtos.BossDTO;
import mav.com.hheroes.services.dtos.JoueurDTO;
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
	private static final String URL_HHEROES_CONTENT = "https://content.hentaiheroes.com";
	private static final String URL_HOME = URL_HHEROES + "/home.html";
	private static final String URL_HAREM = URL_HHEROES + "/harem.html";
	private static final String URL_SHOP = URL_HHEROES + "/shop.html";
	private static final String URL_BATTLE = URL_HHEROES + "/battle.html";
	private static final String URL_MISSIONS = URL_HHEROES + "/activities.html?tab=missions";
	private static final String URL_LOGIN = URL_HHEROES + "/phoenix-ajax.php";
	private static final String URL_ACTION = URL_HHEROES + "/ajax.php";

	public static final String COOKIES = "cookies";
	public static final String LANGUAGE = "lang";
	private static final String STAY_ONLINE = "stay_online";
	private static final String DEFAULT_LOCALE = "fr-FR";
	public static final String LOGIN = "login";

	private String locale;
	private Map<String, Map<String, String>> cookies = new HashMap<>();

	public GameService() {
		// français par défaut
		locale = DEFAULT_LOCALE;
	}

	public Map<String, String> login(String mail, String password) throws AuthenticationException {
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

		if (StringUtils.isEmpty(res.cookie(STAY_ONLINE))) {
			throw new AuthenticationException("Les identifiants n'ont pas réussi à authentifier l'utilisateur");
		}

		setCookies(mail, res.cookies());
		return getCookies(mail);
	}

	public Document getHarem(String login) throws IOException {
		return getPage(URL_HAREM, login);
	}

	public Document getShop(String login) throws IOException {
		return getPage(URL_SHOP, login);
	}

	public Document getMissions(String login) throws IOException {
		return getPage(URL_MISSIONS, login);
	}

	public Document getHome(String login) throws IOException {
		return getPage(URL_HOME, login);
	}

	private Document getPage(String url, String login) throws IOException {
		return Jsoup.connect(url)
				.cookies(getCookies(login))
				.header("Accept-Language", getLocale())
				.parser(Parser.htmlParser())
				.get();
	}

	public byte[] getGirlImage(String url, String login) throws IOException {
		return getImage(url.startsWith("http") ? url : URL_HHEROES + url, login);
	}

	public Map<String, String> getCookies(String login) {
		return cookies.get(login);
	}

	public void setCookies(String login, Map<String, String> cookies) {
		Map<String, String> c = this.cookies.get(login);
		if (c != null) {
			c.putAll(cookies);
		} else {
			this.cookies.put(login, cookies);
		}
	}

	public boolean isConnected() {
		return cookies != null;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String language) {
		if (language == null) {
			this.locale = DEFAULT_LOCALE;
		} else {
			this.locale = language;
		}
	}

	public byte[] getAvatar(Integer girlId, Integer grade, String login) throws IOException {
		return getImage(String.format("%s/pictures/girls/%s/ava%s.png", URL_HHEROES_CONTENT, girlId, grade), login);
	}

	public byte[] getImage(String urlImage, String login) throws IOException {
		try {
			Response resultImageResponse = Jsoup
					.connect(urlImage)
					.cookies(getCookies(login))
					.method(Method.GET)
					.ignoreContentType(true)
					.execute();

			return resultImageResponse.bodyAsBytes();
		} catch (IOException e) {
			System.out.println("Can't get image: " + urlImage);
		}

		return null;
	}

	/**
	 * Réponse au format <code> {"time":4320,"money":2760,"success":true}</code>
	 * 
	 * @param girlId
	 * @return
	 * @throws IOException
	 */
	public SalaryDTO getSalary(Integer girlId, String login) throws IOException {
		Map<String, String> data = new HashMap<>();
		data.put("class", "Girl");
		data.put("action", "get_salary");
		data.put("who", girlId.toString());
		Response result = doPost(URL_ACTION, login, data);

		if (result.statusCode() == HttpStatus.OK.value()) {
			return new ObjectMapper().readValue(result.body(), SalaryDTO.class);
		}

		return null;
	}

	public void acceptMission(Mission mission, String login) throws IOException {
		Map<String, String> data = new HashMap<>();
		data.put("class", "Missions");
		data.put("action", "start_mission");
		data.put("id_mission", mission.getId());
		data.put("id_member_mission", mission.getIdMember());

		doPost(URL_ACTION, login, data);
	}

	public ResponseDTO fightBoss(BossDTO boss, String login) throws IOException {
		Objects.requireNonNull(boss, "Le boss ne doit pas être null");

		Map<String, String> data = new HashMap<>();
		data.put("class", "Battle");
		data.put("action", "fight");
		data.put("who[id_troll]", boss.getId());
		data.put("who[orgasm]", String.valueOf(boss.getOrgasm()));
		data.put("who[ego]", String.valueOf(boss.getEgo()));
		data.put("who[x]", String.valueOf(boss.getX().intValue()));
		data.put("who[d]", String.valueOf(boss.getD()));
		data.put("who[nb_org]", String.valueOf(boss.getNbOrg()));
		data.put("who[figure]", String.valueOf(boss.getFigure()));
		data.put("who[id_world]", boss.getWorld());

		Response res = doPost(URL_ACTION, login, data);

		ResponseDTO response = new ObjectMapper().readValue(res.body(), ResponseDTO.class);
		if (response.getSuccess()) {
			response.getReward().setDrops(Jsoup.parse(response.getReward().getDrops()).text());
		}
		return response;
	}

	public Document getBattle(int arena, String login) throws IOException {
		return getPage(URL_BATTLE + "?id_arena=" + arena, login);
	}

	public ResponseDTO fightJoueur(JoueurDTO joueur, String login) throws IOException {
		Objects.requireNonNull(joueur, "Le joueur ne doit pas être null");

		Map<String, String> data = new HashMap<>();
		data.put("class", "Battle");
		data.put("action", "fight");
		data.put("who[id_member]", joueur.getId());
		data.put("who[orgasm]", String.valueOf(joueur.getOrgasm()));
		data.put("who[ego]", String.valueOf(joueur.getEgo()));
		data.put("who[x]", String.valueOf(joueur.getX().intValue()));
		data.put("who[d]", String.valueOf(joueur.getCurrentEgo()));
		data.put("who[nb_org]", String.valueOf(joueur.getNbOrg()));
		data.put("who[figure]", String.valueOf(joueur.getFigure()));
		data.put("who[id_arena]", joueur.getArena());

		Response res = doPost(URL_ACTION, login, data);

		ResponseDTO response = new ObjectMapper().readValue(res.body(), ResponseDTO.class);
		if (response.getSuccess()) {
			response.getReward().setDrops(Jsoup.parse(response.getReward().getDrops()).text());
		}
		return response;
	}

	/**
	 * Fait une requête POST.
	 * 
	 * @param url
	 *            url demandée.
	 * @param login
	 *            identifiant de l'utilisateur.
	 * @param data
	 *            données à envoyer.
	 * @return La réponse.
	 * @throws IOException
	 */
	private Response doPost(String url, String login, Map<String, String> data) throws IOException {
		Response res = Jsoup.connect(url)
				.cookies(getCookies(login))
				.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.data(data)
				.method(Method.POST)
				.ignoreContentType(true)
				.execute();
		setCookies(login, res.cookies());
		return res;
	}
}
