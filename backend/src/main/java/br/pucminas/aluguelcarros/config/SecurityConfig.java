package br.pucminas.aluguelcarros.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de segurança (autenticação obrigatória).
 *
 * Usamos HTTP Basic: em toda requisição o front-end manda o cabeçalho
 *   Authorization: Basic base64(login:senha)
 * e o Spring Security confere o login e a senha no banco.
 *
 * Regras de acesso:
 *  - /api/auth/cadastro  -> livre (para o cliente criar a conta)
 *  - /api/cliente/**     -> só quem tem perfil CLIENTE
 *  - /api/agente/**      -> só quem tem perfil AGENTE
 *  - qualquer outra rota -> precisa estar logado
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF protege formulários com sessão/cookie; como usamos Basic sem sessão, desligamos
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sessao -> sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(regras -> regras
                .requestMatchers(HttpMethod.POST, "/api/auth/cadastro").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/api/cliente/**").hasRole("CLIENTE")
                .requestMatchers("/api/agente/**").hasRole("AGENTE")
                .anyRequest().authenticated()
            )
            // Quando o login falha, responde 401 em JSON
            // (sem o cabeçalho WWW-Authenticate, para o navegador não abrir aquela janelinha de senha)
            .httpBasic(basic -> basic.authenticationEntryPoint((request, response, erro) -> {
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"mensagem\":\"Login ou senha inválidos\"}");
            }));

        return http.build();
    }

    // As senhas são guardadas criptografadas com BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
