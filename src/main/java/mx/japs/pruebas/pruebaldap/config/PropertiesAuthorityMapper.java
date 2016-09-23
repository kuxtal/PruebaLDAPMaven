package mx.japs.pruebas.pruebaldap.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@PropertySource("classpath:rol_mapping.properties")
public class PropertiesAuthorityMapper implements GrantedAuthoritiesMapper, InitializingBean {
	private static final Logger logger = LogManager.getLogger(PropertiesAuthorityMapper.class);

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

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.isTrue(!(convertToUpperCase && convertToLowerCase),
				"Either convertToUpperCase or convertToLowerCase can be set to true, but not both");
	}

	@Override
	public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
		List<GrantedAuthority> mapped = new ArrayList<GrantedAuthority>();

		for (GrantedAuthority authority : authorities) {
			String strRole = env.getProperty(authority.getAuthority());
			logger.debug("AUTHORITY USUARIO: {}", authority.getAuthority());
			
			String[] permisos = strRole.split(",");
			for(String permiso : permisos){
				mapped.add(mapAuthority(permiso));
			}
		}

		if (defaultAuthority != null) {
			mapped.add(defaultAuthority);
		}

		return mapped;
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

}
