package com.lambdatauri.controllers;

import com.lambdatauri.entities.User;
import com.lambdatauri.security.auth.AuthService;
import com.lambdatauri.security.pojo.AuthenticationRequest;
import com.lambdatauri.security.pojo.AuthenticationResponse;
import com.lambdatauri.security.pojo.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }


//    @PostMapping("/recover-password")
//    public ResponseEntity<?> recoverPassword(
//            @RequestBody Map<String,String> request
//    ) {
//        if(request != null){
//            String username = request.get("email");
//            if(username != null) {
//                log.info(username);
//                authenticationService.recoverPassword(username);
//            }
//        }
//        return ResponseEntity.ok().build();
//    }

//    @Secured({"USER", "ADMIN"})
//    @PostMapping("/change-password")
//    public ResponseEntity<?> changePassword(
//            @RequestBody Map<String,String> request,
//            Authentication authentication
//    ) {
//        if(request != null){
//            String password = request.get("password");
//            if(password != null) {
//                log.info(password);
//                User authedUser = (User) authentication.getPrincipal();
//                authenticationService.changePassword(password, authedUser);
//            }
//        }
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

//    @GetMapping(path = "/confirm")
//    public ResponseEntity<AuthenticationResponse> confirm(@RequestParam("token") String tkn) {
//        String response = emailService.confirmToken(tkn);
//        return ResponseEntity.ok(
//                AuthenticationResponse.builder()
//                        .message(response)
//                        .token(tkn)
//                        .build()
//        );
//    }

//    @PostMapping("/verify")
//    public ResponseEntity<AuthenticationResponse> verify(@RequestBody AuthenticationRequest request){
//        return ResponseEntity.ok(authenticationService.reVerifyEmail(request));
//    }

}
