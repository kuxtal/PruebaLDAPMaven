package mx.japs.pruebas.pruebaldap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableAutoConfiguration
public class Aplicacion extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(Aplicacion.class, args);
	}
	
	 @Override
	 public void addViewControllers(ViewControllerRegistry registry) {
		 registry.addViewController("/login").setViewName("login");
	 }
}
