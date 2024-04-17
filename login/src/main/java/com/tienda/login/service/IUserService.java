package com.tienda.login.service;

import com.tienda.login.model.User;

import java.util.List;

public interface IUserService {
    List<User> findAll();
    User save(User user);

    boolean existsByUsername(String username);
}
