package com.ndr.socialasteroids.security.comunication;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndr.socialasteroids.business.DTOs.UserDTO;
import com.ndr.socialasteroids.business.service.UserService;
import com.ndr.socialasteroids.presentation.payload.request.user.CreateUserRequest;
import com.ndr.socialasteroids.presentation.payload.request.user.LoginRequest;
import com.ndr.socialasteroids.security.service.AuthService;
import com.ndr.socialasteroids.security.utils.JwtUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController
{
    private final @NonNull AuthService authService;
    private final @NonNull UserService userService;
    private final @NonNull JwtUtils jwtUtils;


    @PostMapping(path = "/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid CreateUserRequest request)
    {
        UserDTO newUser = userService.createUser(request.getUsername(), request.getEmail(), request.getPassword());

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest)
    {
        UserDTO userDTO = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

        ResponseCookie jwtCookie = authService.createJwtCookie();
        ResponseCookie refreshTokenCookie = authService.createRefreshTokenCookie(userDTO.getId());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString(), refreshTokenCookie.toString())
                .body(userDTO);
    }
    
    @PostMapping(path = "/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken)
    {
        authService.authWithRefreshToken(refreshToken);
        ResponseCookie newJwtCookie = authService.createJwtCookie();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, newJwtCookie.toString()).build();
    }
    
    @GetMapping(path = "/logout")
    public ResponseEntity<?> logout()
    {
        authService.removeRefreshToken();
        ResponseCookie cleanJwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie cleanRefreshTokenCookie = jwtUtils.getCleanRefreshTokenCookie();
        
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cleanJwtCookie.toString(), cleanRefreshTokenCookie.toString()).build();
    }
}
