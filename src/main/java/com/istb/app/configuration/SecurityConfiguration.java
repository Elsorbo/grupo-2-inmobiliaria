
package com.istb.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthDetailService authService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.cors().and().csrf().disable();

		http.authorizeRequests()
			.antMatchers("/", "/images/**", "/css/**", "/js/**").permitAll()
			.antMatchers("/empleados", "/empleado/**").hasRole("ADMINISTRADOR")
			.anyRequest().authenticated();
		
		http.formLogin()
				.loginPage("/accounts/login")
				.permitAll()
				.defaultSuccessUrl("/dashboard")
			.and()
				.logout()
				.logoutRequestMatcher( new AntPathRequestMatcher("/logout") )
				.logoutSuccessUrl("/");
		
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) 
		throws Exception {
	
		auth.userDetailsService(authService)
			.passwordEncoder(
				PasswordEncoderFactories.createDelegatingPasswordEncoder() );
	
	}

}
