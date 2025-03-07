package com.order_manager.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {

    private final UserDetails principal;

    public CustomJwtAuthenticationToken(Jwt jwt, UserDetails principal, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
        this.principal = principal;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
