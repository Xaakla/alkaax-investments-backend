package com.xaakla.alkaaxinvestments.api.controller;

import com.xaakla.alkaaxinvestments.api.model.auth.signin.SigninReqModel;
import com.xaakla.alkaaxinvestments.api.model.auth.signup.SignupReqModel;
import com.xaakla.alkaaxinvestments.domain.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @PostMapping("signin")
    public ResponseEntity signin(@RequestBody @Valid SigninReqModel signinReqModel) {
        return authService.signin(signinReqModel);
    }

    @PostMapping("signup")
    public ResponseEntity signup(@RequestBody @Valid SignupReqModel signupReqModel) {
        return authService.signup(signupReqModel);
    }
}
