package com.xaakla.alkaaxinvestments.domain.service;

import com.xaakla.alkaaxinvestments.api.model.auth.signin.SigninReqModel;
import com.xaakla.alkaaxinvestments.api.model.auth.signup.SignupReqModel;
import com.xaakla.alkaaxinvestments.domain.model.User;
import com.xaakla.alkaaxinvestments.domain.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    UserRepository userRepository;

    public AuthService(
        UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public ResponseEntity signin(SigninReqModel signinReqModel) {
        if (!userRepository.existsByEmail(signinReqModel.getEmail())) {
            return ResponseEntity.status(400).body("email não existe");
        }

        if (!Objects.equals(userRepository.findByEmail(signinReqModel.getEmail()).getPassword(), signinReqModel.getPassword())) {
            return ResponseEntity.status(400).body("senha incorreta");
        }

        return ResponseEntity.status(200).body("você está logado");
    }

    public ResponseEntity signup(SignupReqModel signupReqModel) {
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

        User newUser = new User(signupReqModel);

        userRepository.save(newUser);

        return ResponseEntity.status(201).build();
    }
}
