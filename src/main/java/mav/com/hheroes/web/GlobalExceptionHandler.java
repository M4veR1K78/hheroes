package mav.com.hheroes.web;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
	private final Logger logger = LogManager.getLogger(getClass());
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
    public ErrorResponse genericExceptionHandler(Exception exception, final HttpServletResponse response) {
		exception.printStackTrace();
		logger.debug(exception);
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ErrorResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
	
	public class ErrorResponse {
		private String message;
		private Integer code;
		
		public ErrorResponse(String message, Integer code) {
			this.message = message;
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
	}
}
