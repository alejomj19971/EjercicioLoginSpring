package com.tienda.login.security.filters;

import ch.qos.logback.core.boolex.EvaluationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.login.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static  com.tienda.login.security.TokenJwtConfig.*;

public class JwtAutenthicationFilter extends UsernamePasswordAuthenticationFilter {
    private  AuthenticationManager authenticationManager;

    // constructor insertado y metodo sobrescrito
    public JwtAutenthicationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        // borre  authenticationManager1
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        User user =null; // hice un cambio
        String username=null;
        String password=null;

        //convertir json a objeto tipo user

        try {
            user= new ObjectMapper().readValue(request.getInputStream(),User.class);
            username=user.getUsername();
            password=user.getPassword();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }





        //Llegan los datos

        System.out.println( username + password);
        UsernamePasswordAuthenticationToken authenticationToken =new UsernamePasswordAuthenticationToken(username,password);
        // No se autentico
        System.out.println(authenticationToken);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {



        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)
                authResult.getPrincipal();

        String username= user.getUsername();

        Collection <? extends GrantedAuthority> roles = authResult.getAuthorities();

        Claims claims= Jwts.claims()
                .add("authorities",roles)
                .add("username",username)
                .build();
        String token = Jwts.builder()

                .subject(username)
                .expiration(new Date(System.currentTimeMillis()+3600000))
                .issuedAt(new Date())
                .claims(claims)
                .signWith(SECRET_KEY)
                .compact();

        response.addHeader(HEADER_AUTHORIZATION,PREFIX_TOKEN+token);
        Map<String,String> body = new HashMap<>();
        body.put("token",token);
        body.put("username",username);
        body.put("message",String.format(" Hola %s has iniciado sesión con exito!",username));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse
            response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("no fue autorizado ");
        Map<String,String> body = new HashMap<>();
        body.put("message","Error en la autenticación  username o password incorrectos!");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(CONTENT_TYPE);


    }
}
