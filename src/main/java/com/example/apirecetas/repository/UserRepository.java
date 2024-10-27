package com.example.apirecetas.repository;

import com.example.apirecetas.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Long,User> {

    User findByUsername(String username);
}
