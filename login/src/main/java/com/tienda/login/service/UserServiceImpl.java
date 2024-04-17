package com.tienda.login.service;

import com.tienda.login.model.Role;
import com.tienda.login.model.User;
import com.tienda.login.repository.RoleRepository;
import com.tienda.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements  IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {
        Optional<Role> optionalRoleUser = roleRepository.findByName("USER");
        List<Role> roles =new ArrayList<>();
       optionalRoleUser.ifPresent(roles::add);
       if(user.isAdmin()){
           Optional<Role> optionalRoleAdmin=roleRepository.findByName("ADMIN");
           optionalRoleAdmin.ifPresent(roles::add);

       }
       user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username){

        return userRepository.existsByUsername(username);
    }


}
