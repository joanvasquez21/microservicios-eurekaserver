package springboot.servicio.zuulserver.oauth;

import java.util.Arrays;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Value("${config.security.ouath.jwt.key}")
	private String JwtKey;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
	http.authorizeRequests()
			.antMatchers("/api/security/oauth/**").permitAll()
			.antMatchers(HttpMethod.GET, "/api/productos/listar", "/api/items/listar", "/api/usuarios/usuarios/{id}")
			.hasAnyRole("ADMIN, USER")
			.antMatchers(HttpMethod.POST, "/api/productos/crear", "/api/items/crear" , "/api/usuarios/usuarios/{id}").hasRole("ADMIN")
			.antMatchers(HttpMethod.PUT, "/api/productos/editar/{id}", "/api/items/editar/{id}", "/api/usuarios/usuarios/{id}").hasRole("ADMIN")
			.antMatchers(HttpMethod.DELETE, "/api/productos/eliminar/{id}", "/api/items/eliminar/{id}", "/api/usuarios/usuarios/{id}").hasRole("ADMIN")
			//Cualquier otra ruta que no este configurada especeficada anteriormente requiera autentication
			.anyRequest().authenticated()
			.and().cors().configurationSource(corsCorsConfigurationSource());
			/**
			 * 
			 *o bien simplificamos con una sola linea de codigo
			 *.antMatchers("/api/productos/**", "/api/items/**", "/api/usuarios/**").hasRole("ADMIN");

			 */
	}

	@Bean
	public CorsConfigurationSource corsCorsConfigurationSource() {
		// TODO Auto-generated method stub
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOrigins(Arrays.asList("*"));
		//permitir los metodos http
		corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		corsConfig.setAllowCredentials(true);
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		
		return source;
	}
	
	//Componente para registrar un filtro de cors, para que quede configurado a nivel global en toda nuestra aplicacion
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsCorsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	
	
	//proteger nuestras rutas y endpoints
	
	//para configurar el token con la misma estructura del servidor de autoconfiguracion
	
	@Bean
	public JwtTokenStore tokenStore() {
		// Para poder crear el token y almacenarlo(JwtTokenStore )
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		// firma
		tokenConverter.setSigningKey(Base64.getEncoder().encodeToString(JwtKey.getBytes()));
		return tokenConverter;
	}
	
	
}
