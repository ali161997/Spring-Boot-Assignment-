package com.santechture.api.service;

import com.santechture.api.dto.GeneralResponse;
import com.santechture.api.dto.admin.AdminDto;
import com.santechture.api.entity.Admin;
import com.santechture.api.exception.BusinessExceptions;
import com.santechture.api.repository.AdminRepository;
import com.santechture.api.security.JwtHelper;
import com.santechture.api.validation.LoginRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager manager;

    private final AdminRepository adminRepository;
    private JwtHelper helper;

    public ResponseEntity<GeneralResponse> login(LoginRequest request) throws BusinessExceptions {
        System.out.println("request = " + request);
        doAuthenticate(request.getUsername(), request.getPassword());
        Admin admin = adminRepository.findByUsernameIgnoreCase(request.getUsername());
        UserDetails userDetails = userDetailsService.loadUserByUsername(admin.getUsername());
        String token = this.helper.generateToken(userDetails);
        AdminDto adminDto = new AdminDto(admin);
        adminDto.setToken(token);
        return new GeneralResponse().response(adminDto);
    }

    private void doAuthenticate(String username, String password) throws BusinessExceptions {
        System.out.println("username = " + username);
//        Admin admin = this.adminRepository.findByUsernameIgnoreCase(username);
//        if (Objects.isNull(admin) || !admin.getPassword().equals(password)) {
//            System.out.println("admin BusinessExceptions before = " + admin);
//            throw new BusinessExceptions("login.credentials.not.match");
//
//        }


        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        System.out.println("authentication = " + authentication);

        System.out.println("before authentication = " + authentication);
        manager.authenticate(authentication);
        System.out.println("after authentication = " + authentication);


    }
}
