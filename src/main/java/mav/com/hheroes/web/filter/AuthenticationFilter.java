package mav.com.hheroes.web.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mav.com.hheroes.services.GameService;

@Component
public class AuthenticationFilter implements Filter {
	@Autowired
	private GameService gameService;

	@Autowired
	private HttpSession httpSession;

	@Override
	public void destroy() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		if (httpSession.getAttribute(GameService.COOKIES) != null) {
			if (gameService.getCookies() == null) {
				gameService.setCookies((Map<String, String>) httpSession.getAttribute(GameService.COOKIES));
			}
			filterChain.doFilter(request, response);
		} else {
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'êtes pas connecté");
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
