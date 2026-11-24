package com.krillion.auth;

import com.krillion.auth.dto.UserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Requires login (see SecurityConfig). Score history endpoints will be added under /api/me later. */
@RestController
@RequestMapping("/api/me")
public class MeController {

    private final AuthService authService;

    public MeController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public UserResponse me(Authentication authentication) {
        return authService.profile(authentication.getName());
    }
}
