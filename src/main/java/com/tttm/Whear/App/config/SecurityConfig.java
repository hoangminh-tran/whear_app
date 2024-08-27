package com.tttm.Whear.App.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.tttm.Whear.App.constant.APIConstant;
import com.tttm.Whear.App.enums.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final RestAccessDenyEntryPoint restAccessDenyEntryPoint;
    private final RestUnauthorizedEntryPoint restUnauthorizedEntryPoint;
    private final LogoutHandler logoutHandler;
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/ws/**"
    };


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)

                //Set access denied handler
                .exceptionHandling(access -> access.accessDeniedHandler(restAccessDenyEntryPoint))

                //Set unauthorized handler
                .exceptionHandling(access -> access.authenticationEntryPoint(restUnauthorizedEntryPoint))

//                .authorizeHttpRequests(
//                        auth ->
//                                auth.requestMatchers(WHITE_LIST_URL)
//                                    .permitAll()
//                                    .requestMatchers(APIConstant.HistoryAPI.SUB_HTTP_OF_HISTORY)
//                                    .hasRole(ERole.CUSTOMER.name())
//                                    .anyRequest()
//                                    .authenticated()
//                )

                .authorizeHttpRequests(auth ->
                                auth.anyRequest()
                                    .permitAll()
                )

                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new SimpleCORSFilter(), WebAsyncManagerIntegrationFilter.class)
                .logout(logout ->
                    logout.logoutUrl(APIConstant.AuthenticationAPI.LOG_OUT)
                            .addLogoutHandler(logoutHandler)
                            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
        );

        return httpSecurity.build();
    }
}
