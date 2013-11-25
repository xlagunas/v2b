package net.i2cat.csade.configuration;

import net.i2cat.csade.services.UserService;
import net.i2cat.csade.web.authentication.JSONFailureHandler;
import net.i2cat.csade.web.authentication.JSONSuccessHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
public class SecurityContext extends WebSecurityConfigurerAdapter{
	
	@Autowired private UserService userService;
	
	@Bean
	public AuthenticationSuccessHandler successHandler(){
		return new JSONSuccessHandler();
	}
	
	@Bean
	public AuthenticationFailureHandler failureHandler(){
		return new JSONFailureHandler();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)	throws Exception {
		auth.userDetailsService(userService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/login").permitAll()
		.and()
			.authorizeRequests().anyRequest().authenticated()
		.and()
		.	formLogin().successHandler(successHandler()).failureHandler(failureHandler())
		.and()
			.csrf().disable();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/socket/**");
	}
	
	
	
	

}
