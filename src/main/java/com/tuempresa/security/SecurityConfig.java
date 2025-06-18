package com.tuempresa.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        JsonAuthenticationFilter jsonAuthFilter = new JsonAuthenticationFilter(authenticationManager);
        
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/trainee/create-trainee", "/trainer/create-trainer").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jsonAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(form -> form.disable())  // Asegúrate de deshabilitar formLogin
            .httpBasic(basic -> basic.disable());

        return http.build();
    }



//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//	
//	@Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
//        JsonAuthenticationFilter jsonAuthFilter = new JsonAuthenticationFilter(authenticationManager);
//        
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/trainee/create-trainee", "/trainer/create-trainer").permitAll()
//                .anyRequest().authenticated()
//            )
//            .sessionManagement(session -> session
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            )
//            .addFilterBefore(jsonAuthFilter, UsernamePasswordAuthenticationFilter.class)
//            .httpBasic(basic -> basic.disable());
//
//        return http.build();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}

//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.csrf(csrf -> csrf.disable())
//				.authorizeHttpRequests(authorize -> authorize
//						.requestMatchers("/trainee/create-trainee", "/trainer/create-trainer", "/login").permitAll()
//						.anyRequest().authenticated())
//
//				.formLogin(form -> form.disable()
////            .formLogin(form -> form
////                .loginProcessingUrl("/login")
////                .permitAll()
//				).httpBasic(basic -> basic.disable());
//
//		return http.build();

//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	// Añade este método para exponer el AuthenticationManager
//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//		return authConfig.getAuthenticationManager();
//	}
//}