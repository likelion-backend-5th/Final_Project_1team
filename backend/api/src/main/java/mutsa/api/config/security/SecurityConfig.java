package mutsa.api.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import mutsa.api.config.security.filter.CustomAuthorizationFilter;
import mutsa.api.config.security.filter.JsonUsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAuthorizationFilter customAuthorizationFilter;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultOAuth2UserService;
    private final AuthenticationSuccessHandler redirectAuthenticationSuccessHandler;
    private final AuthenticationFailureHandler redirectAuthenticationFailureHandler;
    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final AuthenticationFailureHandler customAuthenticationFailureHandler;
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> httOAuth2AuthorizationRequestAuthorizationRequestRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDetailsService userDetailsService;

    @Value("${frontendUrl}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(handle ->
                                           handle.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(
                        jsonUsernamePasswordAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

        httpSecurity.sessionManagement(session ->
                                               session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.authorizeHttpRequests(registry -> registry.anyRequest().permitAll());

        httpSecurity.oauth2Login(oAuth2LoginConfigurer ->
                                         oAuth2LoginConfigurer
                                                 .authorizationEndpoint(
                                                         authorizationEndpointConfig ->
                                                                 authorizationEndpointConfig.authorizationRequestRepository(
                                                                                 httOAuth2AuthorizationRequestAuthorizationRequestRepository)
                                                                         .baseUri("/oauth2/authorization"))
                                                 .redirectionEndpoint(redirectionEndpointConfig ->
                                                                              redirectionEndpointConfig.baseUri(
                                                                                      "/login/oauth2/callback/**"))
                                                 .userInfoEndpoint(userInfoEndpointConfig ->
                                                                           userInfoEndpointConfig.userService(
                                                                                   defaultOAuth2UserService))
                                                 .successHandler(redirectAuthenticationSuccessHandler)
                                                 .failureHandler(redirectAuthenticationFailureHandler)
        ).logout(logout ->
                         logout
                                 .logoutSuccessHandler((request, response, authentication) -> {
                                     response.setStatus(HttpStatus.NO_CONTENT.value());
                                     response.sendRedirect("/login");
                                 })
                                 .clearAuthentication(true)
                                 .logoutRequestMatcher(new AntPathRequestMatcher("/api/security/logout")
                                 ));

        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "POST", "GET", "DELETE", "PUT"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Configuration
    @EnableWebMvc
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:5173")
                    .allowedMethods("*")
                    .allowCredentials(false)
                    .maxAge(3000);
        }
    }

    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter() {
        JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter =
                new JsonUsernamePasswordAuthenticationFilter(
                        objectMapper,
                        customAuthenticationSuccessHandler,
                        customAuthenticationFailureHandler
                );
        jsonUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        return jsonUsernamePasswordAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userDetailsService);

        return new ProviderManager(provider);
    }
}
