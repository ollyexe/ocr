package com.lambdatauri.security.auth;

import com.lambdatauri.entities.User;
import com.lambdatauri.entities.utils.Role;
import com.lambdatauri.exceptions.BadRequestException;
import com.lambdatauri.repository.UserRepository;
import com.lambdatauri.security.config.JwtService;
import com.lambdatauri.security.pojo.AuthenticationRequest;
import com.lambdatauri.security.pojo.AuthenticationResponse;
import com.lambdatauri.security.pojo.RegisterRequest;
import com.lambdatauri.exceptions.UnauthorizedException;
import com.lambdatauri.security.token.Token;
import com.lambdatauri.security.token.TokenRepository;
import com.lambdatauri.security.utils.StrengthChecker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .verified(false)
                .active(false)
                .build();
        if (!StrengthChecker.isValidPassword(request.getPassword())) {
            log.error("Password is weak");
            throw new BadRequestException("[weak-password]");
        }
        var possibleUser = userRepository.findByEmail(request.getEmail());

        if (possibleUser.isEmpty()) {
            var savedUser = userRepository.save(user);
            var jwtToken = jwtService.generateToken(savedUser.getId(), user);
            saveUserToken(savedUser, jwtToken);
            //TODO: Send email to user
            String response = "Please Verify your account by confirming your e-mail address.";
            return AuthenticationResponse.builder()
                    .message(response)
                    .token(jwtToken)
                    .build();
        } else
            log.error("User already exists");
            throw new BadRequestException("User " + request.getEmail() + " already exists.");
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .token(jwtToken)
                .user(user)
                .tokenType(Token.TokenType.BEARER)
                .active(true)
                .build();
        tokenRepository.save(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            throw UnauthorizedException.invalidCredentials();
        }
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user.getId(), user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .message("Authentication Successful.")
                .token(jwtToken)
                .build();
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            tokenRepository.delete(token);
        });
    }
}
