package mav.com.hheroes.services;

import java.io.IOException;
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

	private String locale;
	private Map<String, String> cookies;

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
		
		setCookies(res.cookies());
		return cookies;
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
				.cookies(getCookies())
				.header("Accept-Language", getLocale())
				.parser(Parser.htmlParser())
				.get();
	}

	public byte[] getGirlImage(String url) throws IOException {
		Response resultImageResponse = Jsoup.connect(url.startsWith("http") ? url : URL_HHEROES + url)
				.cookies(getCookies())
				.ignoreContentType(true).execute();

		return resultImageResponse.bodyAsBytes();
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public byte[] getAvatar(Integer girlId, Integer grade) throws IOException {
		Response resultImageResponse = Jsoup
				.connect(String.format("%s/pictures/girls/%s/ava%s.png", URL_HHEROES_CONTENT, girlId, grade))
				.cookies(getCookies())
				.ignoreContentType(true)
				.execute();

		return resultImageResponse.bodyAsBytes();
	}

	public byte[] getImage(String urlImage) throws IOException {
		Response resultImageResponse = Jsoup
				.connect(urlImage)
				.cookies(getCookies())
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
				.cookies(getCookies())			
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
		return cookies != null;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public void acceptMission(Mission mission) throws IOException {
		Jsoup.connect(URL_ACTION)
				.cookies(getCookies())			
				.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.data("class", "Missions")
				.data("action", "start_mission")
				.data("id_mission", mission.getId())
				.data("id_member_mission", mission.getIdMember())
				.method(Method.POST)
				.ignoreContentType(true)
				.execute();
	}

	public ResponseDTO fightBoss(BossDTO boss) throws IOException {
		Objects.requireNonNull(boss, "Le boss ne doit pas être null");
		
		Response res = Jsoup.connect(URL_ACTION)
				.cookies(getCookies())
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

		ResponseDTO response = new ObjectMapper().readValue(res.body(), ResponseDTO.class);
		if (response.getSuccess()) {
			response.getReward().setDrops(Jsoup.parse(response.getReward().getDrops()).text());
		}
		return response;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String language) {
		if (language == null) {
			this.locale = DEFAULT_LOCALE;
		}
		else {
			this.locale = language;			
		}
	}
	
	public Document getBattle(int arena) throws IOException {
		return getPage(URL_BATTLE + "?id_arena=" + arena);
	}
	
	public ResponseDTO fightJoueur(JoueurDTO joueur) throws IOException {
		Objects.requireNonNull(joueur, "Le joueur ne doit pas être null");
		
		Response res = Jsoup.connect(URL_ACTION)
				.cookies(getCookies())
				.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.data("class", "Battle")
				.data("action", "fight")
				.data("who[id_member]", joueur.getId())
				.data("who[orgasm]", String.valueOf(joueur.getOrgasm()))
				.data("who[ego]", String.valueOf(joueur.getEgo()))
				.data("who[x]", String.valueOf(joueur.getX().intValue()))
				.data("who[d]", String.valueOf(joueur.getCurrentEgo()))
				.data("who[nb_org]", String.valueOf(joueur.getNbOrg()))
				.data("who[figure]", String.valueOf(joueur.getFigure()))
				.data("who[id_arena]", joueur.getArena())
				.method(Method.POST)
				.ignoreContentType(true)
				.execute();

		ResponseDTO response = new ObjectMapper().readValue(res.body(), ResponseDTO.class);
		if (response.getSuccess()) {
			response.getReward().setDrops(Jsoup.parse(response.getReward().getDrops()).text());
		}
		return response;
	}
}
