 package springboot.app.oauth.services;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springboot.app.oauth.clients.UsuarioFeignClient;
import springboot.app.commons.usuarios.models.entity.Usuario;

@Service
public class UsuarioService implements UserDetailsService, IUsuarioService {

	private Logger log = LoggerFactory.getLogger(UsuarioService.class);
	
	@Autowired
	private UsuarioFeignClient client;

	//loadUserByUsername se encarga de obtener al usuario por el username
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {		
		//Se comunica con el microservicio usuario
		Usuario usuario = client.findByUsername(username);
		
		if(usuario == null) {
			log.error("error  en el login, no existe el usuario en el sistema");
			throw new UsernameNotFoundException("error  en el login, no existe el usuario en el sistema");
		}
		
		List<GrantedAuthority> authorities  =  usuario.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getNombre()))
				.peek(authority -> log.info("Role: " + authority.getAuthority()))
				.collect(Collectors.toList());
		
		log.info("usuario autenticado" + username);
		
		//retornar la implementacion
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(),true, true, true,authorities);
	}

	
	
	
	@Override
	public Usuario findByUsername(String username) {

		return client.findByUsername(username);
	}




	@Override
	public Usuario update(Usuario usuario, Long id) {
		return client.update(usuario, id);
	}

}
