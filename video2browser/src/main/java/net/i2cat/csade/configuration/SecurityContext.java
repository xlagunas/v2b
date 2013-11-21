package net.i2cat.csade.configuration;

import net.i2cat.csade.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityContext extends WebSecurityConfigurerAdapter{
	
	@Autowired private UserService userService;

	public SecurityContext(){
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth)	throws Exception {

		auth.userDetailsService(userService);
	}
	
	

}
