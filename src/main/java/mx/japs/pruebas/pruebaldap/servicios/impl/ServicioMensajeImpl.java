package mx.japs.pruebas.pruebaldap.servicios.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.japs.pruebas.pruebaldap.servicios.ServicioMensaje;

@Service("servicioInicial")
@Transactional
public class ServicioMensajeImpl implements ServicioMensaje {
	private String mensaje = "Cualquier Usuario Con permiso";
	
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}
