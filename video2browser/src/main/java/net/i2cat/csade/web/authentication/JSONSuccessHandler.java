package net.i2cat.csade.web.authentication;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	   public JSONSuccessHandler() {
	       super();
	       setRedirectStrategy(new NoRedirectStrategy());
	   }

	    @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	            Authentication authentication) throws IOException, ServletException {

	           super.onAuthenticationSuccess(request, response, authentication);
	           ObjectMapper mapper = new ObjectMapper();
	           OutputStream ou = response.getOutputStream();
	           
	           mapper.writeValue(ou, authentication.getPrincipal());
	    }


	    protected class NoRedirectStrategy implements RedirectStrategy {

	        @Override
	        public void sendRedirect(HttpServletRequest request,
	                HttpServletResponse response, String url) throws IOException {
	            // no redirect

	        }

	    }

	}