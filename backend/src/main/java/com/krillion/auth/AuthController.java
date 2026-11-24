package com.krillion.auth;

import com.krillion.auth.dto.LoginRequest;
import com.krillion.auth.dto.RegisterRequest;
import com.krillion.auth.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/** Logout is handled by Spring Security itself: POST /api/auth/logout. */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          SecurityContextRepository securityContextRepository) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest body,
                                 @RequestHeader(value = "X-Player-Id", required = false) UUID anonymousPlayerId,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        UserResponse user = authService.register(body.email(), body.password(), anonymousPlayerId);
        // log the new user in straight away
        startSession(authenticate(user.email(), body.password()), request, response);
        return user;
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginRequest body,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        Authentication authentication = authenticate(body.email(), body.password());
        startSession(authentication, request, response);
        return authService.profile(authentication.getName());
    }

    /** Wrong email or wrong password both throw BadCredentialsException (answered with 401). */
    private Authentication authenticate(String email, String password) {
        return authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(email, password));
    }

    /** Stores the login in the HTTP session, so the browser's session cookie identifies the user. */
    private void startSession(Authentication authentication,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        if (request.getSession(false) != null) {
            request.changeSessionId();
        }
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }
}
