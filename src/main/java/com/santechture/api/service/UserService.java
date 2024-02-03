package com.santechture.api.service;

import com.santechture.api.dto.GeneralResponse;
import com.santechture.api.dto.user.UserDto;
import com.santechture.api.entity.User;
import com.santechture.api.exception.BusinessExceptions;
import com.santechture.api.repository.UserRepository;
import com.santechture.api.security.TokenBlacklist;
import com.santechture.api.validation.AddUserRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {


    private final TokenBlacklist tokenBlacklist;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;


    public ResponseEntity<GeneralResponse> list(Pageable pageable) {
        return new GeneralResponse().response(userRepository.findAll(pageable));
    }

    public ResponseEntity<GeneralResponse> addNewUser(AddUserRequest request) throws BusinessExceptions {

        if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            throw new BusinessExceptions("username.exist");
        } else if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new BusinessExceptions("email.exist");
        }

        User user = new User(request.getUsername(), request.getEmail());
        userRepository.save(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication addNewUser = " + authentication);
        String currentToken = authentication.getCredentials().toString();
        System.out.println("currentToken = " + currentToken);
        tokenBlacklist.blacklistToken(currentToken);

        return new GeneralResponse().response(new UserDto(user));
    }

}
