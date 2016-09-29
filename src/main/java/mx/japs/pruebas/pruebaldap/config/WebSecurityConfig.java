package mx.japs.pruebas.pruebaldap.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
@PropertySource("classpath:app.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final Logger logger = LogManager.getLogger(WebSecurityConfig.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	CustomLdapUserDetailsMapper userDatailMapper;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.debug("configure()");
		http
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.permitAll()
				.and()
			.logout()
				.permitAll();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		logger.debug("configureGlobal()");
		
		auth
			.ldapAuthentication()
				.userDnPatterns(env.getProperty("ldap.userDnPatterns"))
				.groupSearchBase(env.getProperty("ldap.groupSearchBase"))
				.groupSearchFilter(env.getProperty("ldap.groupSearchFilter"))
				.contextSource()
					.root(env.getProperty("ldap.root"))
					.ldif(env.getProperty("ldap.ldif"))
					.and()
				.userDetailsContextMapper(userDatailMapper);
		
//		auth.inMemoryAuthentication().withUser("usuario01").password("password").roles("ADMINISTRADOR");
//		auth.inMemoryAuthentication().withUser("usuario02").password("password").roles("OPERADOR");
//		auth.inMemoryAuthentication().withUser("usuario03").password("password").roles("ADMINISTRADOR","OPERADOR");
	}
}
