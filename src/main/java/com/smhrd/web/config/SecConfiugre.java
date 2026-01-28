package com.smhrd.web.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.smhrd.web.service.Oatuh2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecConfiugre {
	
	private final Oatuh2UserService oatuh2UserService;
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable());
		http.cors(cors->cors.configurationSource(corsConfigurationSource()));
		
		
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("api/**",  "/oauth2/**",
						  "/login/oauth2/**","api/logout").permitAll()
				.requestMatchers("api/admin/**").hasRole("api/ADMIN/**")
				.anyRequest().permitAll());
				

		http.formLogin(login -> login.loginProcessingUrl("/api/login")
				.usernameParameter("loginId")
				.passwordParameter("loginPw")
				// ✅ 성공하면 200 OK로 응답
				.successHandler((req, res, auth) -> {
					res.setStatus(200);
					res.setContentType("application/json;charset=UTF-8");
					res.getWriter().write("{\"result\":\"ok\",\"user\":\"" + auth.getName() + "\"}");
				})
				// ✅ 실패하면 401로 응답
				.failureHandler((req, res, ex) -> {
					  res.setStatus(401);
					  res.setContentType("application/json;charset=UTF-8");
					  res.getWriter().write("{\"result\":\"fail\",\"msg\":\"아이디 또는 비밀번호가 올바르지 않습니다.\"}");
				}));
		http.logout(logout -> logout
				.logoutUrl("/api/logout")
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.deleteCookies("JSESSIONID")
				   .logoutSuccessHandler((req, res, auth) -> {
				        res.setStatus(200);
				        res.setContentType("application/json;charset=UTF-8");
				        res.getWriter().write("{\"result\":\"ok\",\"msg\":\"LOGOUT\"}");
				    }));
		
		http.oauth2Login(oauth2->oauth2
					.userInfoEndpoint(endpoint->endpoint
						.userService(oatuh2UserService))
						.successHandler((req,res,auth)->{
							res.sendRedirect("/");
						})
//						.failureHandler(oatuh2failHandler))
					);
		return http.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		 config.setAllowedOriginPatterns(List.of(
		            "http://localhost:*",
		            "http://127.0.0.1:*"
		        ));
		 // ✅ 세션 쿠키(JSESSIONID)를 주고받으려면 무조건 true
	        config.setAllowCredentials(true);

	        // ✅ 허용 메서드(필요한 것만)
	        config.setAllowedMethods(List.of(
	            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
	        ));

	        // ✅ 보통 전체 허용
	        config.setAllowedHeaders(List.of("*"));

	        // ✅ 프론트에서 헤더를 읽어야 하면 노출 설정(필요 시)
	        config.setExposedHeaders(List.of("Set-Cookie"));

	        // ✅ preflight 캐시 시간
	        config.setMaxAge(3600L);

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        // ✅ 모든 경로에 CORS 적용
	        source.registerCorsConfiguration("/**", config);
	        return source;
	}
}
