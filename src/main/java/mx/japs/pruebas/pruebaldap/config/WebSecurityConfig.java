package mx.japs.pruebas.pruebaldap.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final Logger logger = LogManager.getLogger(WebSecurityConfig.class);

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.debug("configure()");
		http
			.authorizeRequests()
				.anyRequest().fullyAuthenticated()
				.and()
			.formLogin()
			.and()
            .logout();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		logger.debug("configureGlobal()");
		
		auth
			.ldapAuthentication()
				.userDnPatterns("uid={0},ou=personas")
				.groupSearchBase("dc=Aplicacion01,dc=aplicaciones,ou=desarrollo")
				.groupSearchFilter("(member={0})")
				.contextSource().ldif("classpath:CargaINICIAL.ldif")
				.root("dc=japs,dc=mx");
	}
}
