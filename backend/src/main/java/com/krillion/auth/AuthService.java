package com.krillion.auth;

import com.krillion.auth.dto.UserResponse;
import com.krillion.common.BadRequestException;
import com.krillion.common.ConflictException;
import com.krillion.player.Player;
import com.krillion.player.PlayerService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;

@Service
public class AuthService {

    private static final int BCRYPT_MAX_BYTES = 72;

    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final DisplayNameGenerator displayNames;
    private final PlayerService players;

    public AuthService(AppUserRepository users,
                       PasswordEncoder passwordEncoder,
                       DisplayNameGenerator displayNames,
                       PlayerService players) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.displayNames = displayNames;
        this.players = players;
    }

    /**
     * Creates the account and gives it a player. If the browser already had an anonymous
     * player (anonymousPlayerId), that player is kept, so today's progress is not lost.
     */
    @Transactional
    public UserResponse register(String rawEmail, String password, UUID anonymousPlayerId) {
        String email = rawEmail.trim().toLowerCase(Locale.ROOT);

        if (password.getBytes(StandardCharsets.UTF_8).length > BCRYPT_MAX_BYTES) {
            throw new BadRequestException("Password must be at most 72 bytes long");
        }
        if (users.existsByEmail(email)) {
            throw new ConflictException("Email is already registered");
        }

        AppUser user = new AppUser(email, passwordEncoder.encode(password), displayNames.generate());
        try {
            users.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            // two registrations with the same email at the same moment: the unique constraint wins
            throw new ConflictException("Email is already registered");
        }

        UUID playerId = players.attachToUser(anonymousPlayerId, user.getId());
        return new UserResponse(user.getEmail(), user.getDisplayName(), playerId);
    }

    @Transactional(readOnly = true)
    public UserResponse profile(String email) {
        AppUser user = users.findByEmail(email).orElseThrow();
        Player player = players.getByUserId(user.getId());
        return new UserResponse(user.getEmail(), user.getDisplayName(), player.getId());
    }
}
