package mx.japs.pruebas.pruebaldap.controllers;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import mx.japs.pruebas.pruebaldap.servicios.ServicioMensaje;

@Controller
public class InicioController {
	private static final Logger logger = LogManager.getLogger(InicioController.class);
	
	@Autowired
	ServicioMensaje servicioMsj;

	@GetMapping("/")
	public String index(Map<String, Object> model, Principal principal) {
		logger.debug("index()");

		model.put("title", "AREA PUBLICA");
		model.put("username", getUserName(principal));
		model.put("userroles", getUserRoles(principal));
		model.put("message", "Cualquier usuario");

		return "home";
	}

	@GetMapping("/usuarios")
	@PreAuthorize("hasRole('SELECT_MENSAJE')")
	public String usuarios(Map<String, Object> model, Principal principal) {
		logger.debug("usuarios()");

		model.put("title", "AREA SEGURA");
		model.put("username", getUserName(principal));
		model.put("userroles", getUserRoles(principal));
		model.put("message", servicioMsj.getMensaje());

		return "home";
	}

	@GetMapping("/usuarios_modifica")
	@PreAuthorize("hasRole('UPDATE_MENSAJE')")
	public String usuarios_modifica(Map<String, Object> model, Principal principal) {
		logger.debug("usuarios_modifica()");

		
		
		model.put("title", "AREA SEGURA");
		model.put("username", getUserName(principal));
		model.put("userroles", getUserRoles(principal));
		
		servicioMsj.setMensaje("Solo usuarios autorizados Administrador");
		model.put("message", servicioMsj.getMensaje());
		
		return "home";
	}

	private String getUserName(Principal principal) {
		if (principal == null) {
			return "anonymous";
		} else {

			logger.debug("Principal: {}", principal.getName());
			return principal.getName();
		}
	}

	private Collection<String> getUserRoles(Principal principal) {
		logger.debug("getUserRoles({})", principal);

		if (principal == null) {
			return Arrays.asList("none");
		} else {

			Set<String> roles = new HashSet<String>();

			final UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
			Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
			for (GrantedAuthority grantedAuthority : authorities) {
				logger.debug("ROL USUARIO: {} - {}", principal.getName(), grantedAuthority.getAuthority());

				roles.add(grantedAuthority.getAuthority());
			}

			logger.debug("getUserRoles({}): {}", principal, roles);
			return roles;
		}
	}
}
