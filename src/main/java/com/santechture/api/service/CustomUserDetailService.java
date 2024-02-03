package com.santechture.api.service;


import com.santechture.api.entity.Admin;
import com.santechture.api.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Load user by username from database
        Admin admin = adminRepository.findByUsernameIgnoreCase(username);
        if (admin == null)
            throw new UsernameNotFoundException("USer Not Found");
        System.out.println("admin " + admin);
        User user = new User(admin.getUsername(), admin.getPassword(), new ArrayList<>());
        System.out.println("loadUserByUsername usser = " + user);
        return user;
    }

}