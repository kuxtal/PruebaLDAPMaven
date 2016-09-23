package mx.japs.pruebas.pruebaldap.controllers;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InicioController {
	private static final Logger logger = LogManager.getLogger(InicioController.class);
	
	@GetMapping("/")
    public String index() {
		logger.debug("index()");
		
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		for(SimpleGrantedAuthority authoritie : authorities)
			logger.debug("ROL: {}", authoritie.getAuthority());
		
        return "Hola Mundo... Pagina Inicial!";
    }
}
