// SecurityFilter.java
package br.com.pitarelo.api_todo.infraestruture.security;

import br.com.pitarelo.api_todo.application.services.AuthenticationService;
import br.com.pitarelo.api_todo.application.services.TokenService;
import br.com.pitarelo.api_todo.domain.dto.login.TokenData;
import br.com.pitarelo.api_todo.domain.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    private final AuthenticationService authenticationService;

    public SecurityFilter(@Lazy AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull  HttpServletResponse response,
                                    @NotNull  FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String requestMethod = request.getMethod();

        // Re-adicione este trecho. Ele é importante para rotas de autenticação (login/registro)
        // que não possuem token JWT ainda. Ele permite que essas requisições prossigam
        // para serem processadas pelas regras permitAll() no WebSecurityConfig.
        if (path.startsWith("/user")) { // Adapte esta condição se suas rotas de auth não começarem com /user
            filterChain.doFilter(request, response);
            return;
        }

        if (requestMethod.equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }


        // Caso não seja alguma rota pública do /user e não seja informado o token
        String token = getTokenFromRequest(request);
        if (token == null) {
            // Se não há token e não é uma rota pública, envia 401.
            // O AuthenticationEntryPoint do WebSecurityConfig também faria isso,
            // mas ter aqui pode ser mais imediato para requests sem token em rotas protegidas.
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token JWT ausente ou inválido.");
            return;
        }

        try {
            TokenData data = tokenService.getSubject(token);
            User user = authenticationService.loadUserById(data.id());
            var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            // Se o token existe mas é inválido (expirado, modificado, etc.)
            System.err.println("Erro na validação do token: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token JWT inválido.");
            return;
        }

        // Caso dê tudo certo passa para os filtros restantes
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7).trim();
        }
        return null;
    }
}
