package springboot.app.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfiguration{

	@Autowired
	private AuthenticationEventPublisher eventPublisher;
	
	@Autowired
	private UserDetailsService usuarioService;
	
 	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
	}
 	
 	
	@Autowired
	protected void  configure( AuthenticationManagerBuilder auth ) throws Exception{
		auth.userDetailsService(this.usuarioService).passwordEncoder(passwordEncoder())
			.and().authenticationEventPublisher(eventPublisher);
		
	}
	
	//configuramos el autenticacion manager tenemos que registrar como componente de spring para luego inyectar en la config del servidor de oauth2
	/* @Bean
	protected AuthenticationManager authenticationManager() throws Exception{
		return super.auhenticationManager();
		}
	*/
	
	
}
