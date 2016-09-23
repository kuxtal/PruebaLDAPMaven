package mx.japs.pruebas.pruebaldap.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InicioController {
	private static final Logger logger = LogManager.getLogger(InicioController.class);
	
	@GetMapping("/")
    public String index() {
		logger.debug("index()");

        return "Hola Mundo... Pagina Inicial!";
    }
	
	@GetMapping("/usuarios")
	@Secured({"PERM_SELECT_USUARIO"})
    public String usuarios() {
		logger.debug("usuarios()");

        return "Hola Mundo... usuarios!";
    }
	
	@GetMapping("/usuarios_modifica")
	@Secured({"PERM_UPDATE_USUARIO"})
	@PreAuthorize("hasAuthority('PERM_UPDATE_USUARIO')")
    public String usuarios_modifica() {
		logger.debug("usuarios_modifica()");

        return "Hola Mundo... usuarios_modifica!";
    }
}
