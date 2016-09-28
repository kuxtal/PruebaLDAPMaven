package mx.japs.pruebas.pruebaldap.servicios;

import org.springframework.security.access.prepost.PreAuthorize;

public interface ServicioInicial {

	@PreAuthorize("hasRole('SELECT_MENSAJE')")
	public String getMensaje();
	
	@PreAuthorize("hasRole('UPDATE_MENSAJE')")
	public void setMensaje(String mensaje);
}
