package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.api.model.auth.signin.SigninReqModel;
import com.xaakla.alkaaxinvestments.api.model.auth.signup.SignupReqModel;
import com.xaakla.alkaaxinvestments.domain.model.User;
import com.xaakla.alkaaxinvestments.domain.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<Object> signin(SigninReqModel signinReqModel) {
        if (!userRepository.existsByEmail(signinReqModel.getEmail()))
            return ResponseEntity.status(400).body("email não existe");

        if (!passwordEncoder.matches(signinReqModel.getPassword(), userRepository.findByEmail(signinReqModel.getEmail()).getPassword()))
            return ResponseEntity.status(400).body("senha incorreta");

        var authResult = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(signinReqModel.getEmail(), signinReqModel.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authResult);


        return authResult.isAuthenticated() ? ResponseEntity.status(200).body("você está logado") :
                                              ResponseEntity.status(400).body("erro ao logar");
    }

    public ResponseEntity<Object> signup(SignupReqModel signupReqModel) {
        if (userRepository.existsByEmail(signupReqModel.getEmail())) {
            return ResponseEntity
                    .status(400)
                    .body("email '" + signupReqModel.getEmail() + "' already exists");
        }

        if (userRepository.existsByUsername(signupReqModel.getUsername())) {
            return ResponseEntity
                    .status(400)
                    .body("username '" + signupReqModel.getUsername() + "' already exists");
        }

        signupReqModel.setPassword(passwordEncoder.encode(signupReqModel.getPassword()));
        User newUser = new User(signupReqModel);

        userRepository.save(newUser);

        return ResponseEntity.status(201).build();
    }
}
