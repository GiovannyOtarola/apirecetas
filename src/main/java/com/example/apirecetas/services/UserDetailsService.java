package com.example.apirecetas.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService {

     UserDetails loadUserByUsername(String username);
}
