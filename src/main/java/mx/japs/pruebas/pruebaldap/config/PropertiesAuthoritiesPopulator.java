package mx.japs.pruebas.pruebaldap.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@PropertySource("classpath:rol_mapping.properties")
public class PropertiesAuthoritiesPopulator implements LdapAuthoritiesPopulator {
	private static final Logger logger = LogManager.getLogger(PropertiesAuthoritiesPopulator.class);
	
	private GrantedAuthority defaultAuthority;
	private String prefix = "PERM_";
	private boolean convertToUpperCase = false;
	private boolean convertToLowerCase = false;
	
	@Autowired
	private Environment env;
	
	/**
	 * Sets a default authority to be assigned to all users
	 *
	 * @param authority
	 *            the name of the authority to be assigned to all users.
	 */
	public void setDefaultAuthority(String authority) {
		Assert.hasText(authority, "The authority name cannot be set to an empty value");
		this.defaultAuthority = new SimpleGrantedAuthority(authority);
	}
	
	private GrantedAuthority mapAuthority(String name) {
		if (convertToUpperCase) {
			name = name.toUpperCase();
		} else if (convertToLowerCase) {
			name = name.toLowerCase();
		}

		if (prefix.length() > 0 && !name.startsWith(prefix)) {
			name = prefix + name;
		}
		
		logger.debug("mapAuthority(name: {})", name);
		return new SimpleGrantedAuthority(name);
	}

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(mapAuthority("ROLE_USER"));
        
        return authorities;
	}
}
