package com.agil.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
		return new MySimpleUrlAuthenticationSuccessHandler();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/registration").permitAll().antMatchers("/webjars/**").permitAll()
				.antMatchers("/registrationConfirm").permitAll().antMatchers("/images/**").permitAll()
				.antMatchers("/js/**").permitAll().antMatchers("/css/**").permitAll().antMatchers("/dataprotection")
				.permitAll().antMatchers("/register").permitAll().antMatchers("/search/**").permitAll()
				.antMatchers("/alert/**").permitAll().antMatchers("/impressum").permitAll().anyRequest().authenticated()
				.and().formLogin().loginPage("/login").successHandler(myAuthenticationSuccessHandler()).permitAll()
				.and().logout().logoutSuccessUrl("/login").logoutSuccessHandler(logoutSuccessHandler()).permitAll().and().exceptionHandling()
				.accessDeniedPage("/403").and().rememberMe().key("uSecret").and().logout().and().csrf().disable();
	}

	private LogoutSuccessHandler logoutSuccessHandler() {
		return new MyLogoutSuccessHandler();
	}

	@Bean
	public AuthenticationManager customAuthenticationManager() throws Exception {
		return authenticationManager();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

}