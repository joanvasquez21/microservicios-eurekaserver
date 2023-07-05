package springboot.app.oauth.security;

import java.util.Arrays;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@RefreshScope
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	//Reemplazamos las formas literal
	private Environment env;
	// 1.- Es inyectar
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private InfoAdicionalToken infoAdicionalToken;
	
	//Es el permiso que tendran nuestros endpoints del servidor de autorizacion
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		//security para dar seguridad a los tokens
		security.tokenKeyAccess("permitAll()")
		//para validar nuestro token
		.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// Aqui registramos nuestros clientes
		clients.inMemory().withClient(env.getProperty("config.security.oauth.client.id"))
						  .secret(passwordEncoder.encode("config.security.oauth.client.secret"))
						  .scopes("read", "write")
						  .authorizedGrantTypes("password", "refresh_token")
						  .accessTokenValiditySeconds(3600)
						  .refreshTokenValiditySeconds(3600);
	}

	@Override
	@Bean
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		//Tenemos que unir el accestokencovnerter()
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken, accessTokenConverter()));
		
		
		// podemos configurar el token storage
		endpoints.authenticationManager(authenticationManager)
				 .tokenStore(tokenStore())
				 .accessTokenConverter(accessTokenConverter())
				 .tokenEnhancer(tokenEnhancerChain);
	}

	@Bean
	public JwtTokenStore tokenStore() {
		// Para poder crear el token y almacenarlo(JwtTokenStore )
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		// firma
		tokenConverter.setSigningKey(Base64.getEncoder().encodeToString(env.getProperty("config.security.oauth.jwt.key").getBytes()));
		return tokenConverter;
	}

}
