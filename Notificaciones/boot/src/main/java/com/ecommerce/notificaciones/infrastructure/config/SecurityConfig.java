package com.ecommerce.notificaciones.infrastructure.config;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.List;
@Configuration
public class SecurityConfig {
 @Bean SecurityFilterChain security(HttpSecurity http,NotificacionesProperties props) throws Exception {
  http.csrf(c->c.disable()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .requestCache(c->c.disable()).formLogin(c->c.disable()).httpBasic(c->c.disable())
   .authorizeHttpRequests(a->a.requestMatchers("/health").permitAll().anyRequest().authenticated())
   .exceptionHandling(e->e.authenticationEntryPoint((req,res,ex)->error(res,401,"NO_AUTORIZADO"))
    .accessDeniedHandler((req,res,ex)->error(res,403,"ACCESO_DENEGADO")))
   .addFilterBefore(new ApiKeyFilter(props.apiKey()),AnonymousAuthenticationFilter.class);
  return http.build();
 }
 static void error(HttpServletResponse response,int status,String code) throws IOException {
  response.setStatus(status); response.setContentType("application/problem+json"); response.setCharacterEncoding("UTF-8");
  response.setHeader("Cache-Control","no-store");
  response.getWriter().write("{\"type\":\"about:blank\",\"title\":\"Acceso no autorizado\",\"status\":"+status+",\"code\":\""+code+"\"}");
 }
 static final class ApiKeyFilter extends OncePerRequestFilter {
  private final byte[] expected;
  ApiKeyFilter(String key) { expected=digest(key); }
  private static byte[] digest(String key) {
   try { return MessageDigest.getInstance("SHA-256").digest(key.getBytes(StandardCharsets.UTF_8)); }
   catch (NoSuchAlgorithmException ex) { throw new IllegalStateException(ex); }
  }
  @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain) throws IOException,ServletException {
   String key=req.getHeader("X-API-Key");
   if (key!=null && key.length()<=256 && MessageDigest.isEqual(expected,digest(key))) {
    var authentication=new UsernamePasswordAuthenticationToken("microservicio",null,List.of(new SimpleGrantedAuthority("ROLE_SERVICE")));
    SecurityContextHolder.getContext().setAuthentication(authentication);
   }
   chain.doFilter(req,res);
  }
 }
}
