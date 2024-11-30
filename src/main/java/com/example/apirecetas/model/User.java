package com.example.apirecetas.model;


import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String name;

    private String email;

    private String password;

    private String role;

    public User(String username, String name, String email, String password) {
        
    }

    public User() {
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna el rol basado en el campo `role`
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }
    

    
    @Override
    public boolean isAccountNonExpired() {
        // Lógica básica: siempre retorna true (cuenta no expirada)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Lógica básica: siempre retorna true (cuenta no bloqueada)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Lógica básica: siempre retorna true (credenciales no expiradas)
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Lógica básica: siempre retorna true (usuario habilitado)
        return true;
    }

    


}
