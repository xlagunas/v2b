package net.i2cat.csade.configuration;

import net.i2cat.csade.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration

public class SecurityContext extends WebSecurityConfigurerAdapter{
	
	@Autowired private UserService userService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)	throws Exception {
		auth.userDetailsService(userService);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests().antMatchers("/socket/**").permitAll()
		.and()
		.authorizeRequests().anyRequest().authenticated()
		.and()
		.formLogin()
//			.defaultSuccessUrl("/app/index.html")
//			.successHandler(successHandler())
//			.failureHandler(failureHandler())
	.and()
		.authorizeRequests().anyRequest().authenticated()
	.and()
		.csrf().disable();
}

	@Override
	public void configure(WebSecurity web) throws Exception {
		//web
		//	.ignoring().antMatchers("/socket/**");
	}

	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	
	
	
	
	

}
