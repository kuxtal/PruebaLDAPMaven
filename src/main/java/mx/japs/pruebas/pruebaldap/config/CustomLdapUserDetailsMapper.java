package mx.japs.pruebas.pruebaldap.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:rol_mapping.properties")
public class CustomLdapUserDetailsMapper extends LdapUserDetailsMapper {
	private static final Logger logger = LogManager.getLogger(CustomLdapUserDetailsMapper.class);
	
	@Autowired
	private Environment env;
	private GrantedAuthority defaultAuthority;
	private String prefixDefault 		= "ROLE_";
	private String prefixPermDefault	= "";
	
	 @Override
     public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
		 logger.debug("mapUserFromContext: {}", ctx.getNameInNamespace());
		 LdapUserDetailsImpl detalleUsuario = (LdapUserDetailsImpl) super.mapUserFromContext(ctx, username, authorities);
		 List<GrantedAuthority> mapped = new ArrayList<GrantedAuthority>();
		 
		 this.setRolePrefix(prefixPermDefault);
		 for (GrantedAuthority authority : detalleUsuario.getAuthorities()) {
			 String strAuthority = authority.getAuthority().replaceAll(prefixDefault, "");

			 logger.debug("LEYENDO AUTHORITY: {}", strAuthority);
			 String strRole = env.getProperty(strAuthority);
			 
			 if(null != strRole || !"".equals(strRole)){
				 String[] permisos = strRole.split(",");
				 for(String permiso : permisos){
					 logger.debug("AGREGANDO AUTHORITY [{}] AL USUARIO: {}", permiso, username);
					 mapped.add(createAuthority(permiso));
				}
			 }
			 else {
				 logger.debug("NO EXISTE MAPEO DE ROLES {}", authority.getAuthority());
			 }
			 
			 
		 }
		 
		 LdapUserDetailsImpl.Essence detalleUsuarioModif = new LdapUserDetailsImpl.Essence(detalleUsuario);
		 detalleUsuarioModif.setAuthorities(mapped);
		 
		 return detalleUsuarioModif.createUserDetails();
     }
}
