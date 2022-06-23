// package com.ndr.socialasteroids.security.comunication;

// import java.net.URISyntaxException;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseCookie;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.ndr.socialasteroids.business.DTOs.UserDTO;
// import com.ndr.socialasteroids.business.service.UserService;
// import com.ndr.socialasteroids.presentation.payload.request.user.CreateUserRequest;
// import com.ndr.socialasteroids.presentation.payload.request.user.LoginRequest;
// import com.ndr.socialasteroids.presentation.payload.response.JwtResponse;
// import com.ndr.socialasteroids.security.entities.UserDetailsImpl;
// import com.ndr.socialasteroids.security.service.AuthService;
// import com.ndr.socialasteroids.utils.JwtUtils;

// import lombok.NonNull;
// import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("/auth")
// @RequiredArgsConstructor(onConstructor = @__(@Autowired))
// public class AuthController
// {
//     private final @NonNull AuthService authService;
//     private final @NonNull UserService userService;
//     private final @NonNull JwtUtils jwtUtils;

//     @PostMapping(path = "/signup")
//     public ResponseEntity<?> signup(@RequestBody CreateUserRequest request) throws URISyntaxException
//     {
//         System.out.println(request.getPassword());
//         UserDTO newUser = userService.createUser(request.getUsername(), request.getEmail(), request.getPassword());

//         return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
//     }

//     @PostMapping(path = "/refresh-token")
//     //@PreAuthorize("#userId == principal.getUserSecurityInfo().getId()")
//     public ResponseEntity<?> refreshToken(@RequestBody String refreshToken)
//     {
//         UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
//                 .getPrincipal();
        
//         ResponseCookie newJwtCookie = authService.generateJwtFromRefreshToken(refreshToken);
//         String newRefreshTokenCookie = authService.createRefreshToken(userDetails.getUserSecurityInfo().getId());

//         JwtResponse jwtResponse = new JwtResponse(newJwt, newRefreshToken, userDetails.getUserSecurityInfo().getId(),
//                 userDetails.getUsername());

//         return ResponseEntity.ok().body(jwtResponse);
//     }
    
//     @PostMapping(path = "/login")
//     public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest)
//     {
//         UserDTO userDTO = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

//         ResponseCookie jwtCookie = authService.createJwt();
//         String refreshToken = authService.createRefreshToken(userDTO.getId());

//         //JwtResponse jwtResponse = new JwtResponse(jwt, refreshToken, userDTO.getId(), userDTO.getUsername());

//         return ResponseEntity.ok().body(jwtResponse);
//     }
    
//     @GetMapping(path = "/logout")
//     public ResponseEntity<?> logout()
//     {
//         authService.removeRefreshToken();
//         ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        
//         return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
//     }
// }
