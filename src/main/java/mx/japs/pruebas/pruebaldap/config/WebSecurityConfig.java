package mx.japs.pruebas.pruebaldap.config;

import java.util.Collection;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final Logger logger = LogManager.getLogger(WebSecurityConfig.class);

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.debug("configure()");
		http
			.authorizeRequests()
				.mvcMatchers("/**").authenticated()
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
				.userDnPatterns("uid={0},ou=personas")
				.groupSearchBase("dc=Aplicacion01,dc=aplicaciones,ou=desarrollo")
				.groupSearchFilter("(member={0})")
				.contextSource().ldif("classpath:CargaINICIAL.ldif")
				.root("dc=japs,dc=mx")
				.and()
				.ldapAuthoritiesPopulator(new LdapAuthoritiesPopulator() {
					@Override
					public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
						return Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
					}
				});
	}
}
