package com.ribeiroanibal.adopt.rest.controller;

import com.ribeiroanibal.adopt.AdoptApplication;
import com.ribeiroanibal.adopt.exception.EntityExistException;
import com.ribeiroanibal.adopt.model.User;
import com.ribeiroanibal.adopt.rest.dto.JwtResponse;
import com.ribeiroanibal.adopt.rest.dto.LoginRequest;
import com.ribeiroanibal.adopt.rest.dto.UserPostDto;
import com.ribeiroanibal.adopt.security.UserDetailsImpl;
import com.ribeiroanibal.adopt.security.jwt.JwtTokenUtil;
import com.ribeiroanibal.adopt.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(AdoptApplication.API_AUTH)
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(final AuthenticationManager authenticationManager,
                          final UserService userService,
                          final PasswordEncoder encoder,
                          final JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.encoder = encoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwt = jwtTokenUtil.generateJwtToken(authentication);

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(
                new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername())
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserPostDto userPostDto) {
        if (userService.getOptionalEntityByUsername(userPostDto.getUsername()).isPresent()) {
            throw new EntityExistException(User.class.getSimpleName(), userPostDto.getUsername());
        }

        final User user = new User(userPostDto.getUsername(),
                encoder.encode(userPostDto.getPassword()),
                userPostDto.getPhone());

        return new ResponseEntity<>(userService.addEntity(user), HttpStatus.CREATED);
    }
}
