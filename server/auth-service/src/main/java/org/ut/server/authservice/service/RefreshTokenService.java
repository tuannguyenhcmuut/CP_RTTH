package org.ut.server.authservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.ut.server.authservice.config.JwtUtils;
import org.ut.server.authservice.dto.request.TokenRefreshRequest;
import org.ut.server.authservice.dto.response.TokenRefreshResponseDto;
import org.ut.server.authservice.exception.TokenRefreshException;
import org.ut.server.authservice.model.Account;
import org.ut.server.authservice.model.CustomUserDetails;
import org.ut.server.authservice.model.RefreshToken;
import org.ut.server.authservice.repository.AccountRepository;
import org.ut.server.authservice.repository.RefreshTokenRepository;

import javax.transaction.Transactional;
import java.sql.Ref;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenService {
    @Value("${oms.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserDetailsService userDetailsService;



    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();
        // TODO: check the refresh token of the user if it exists, if it does, delete it


        refreshToken.setUserAccount(accountRepository.findAccountByUsername(username).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);

        log.error("Refresh token expired at: " + refreshToken.getExpiryDate());

        return refreshToken;
    }

    public RefreshToken createRefreshToken(String username, Instant expiryDate) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUserAccount(accountRepository.findAccountByUsername(username).get());
        refreshToken.setExpiryDate(expiryDate);
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);

        log.error("Refresh token expired at: " + refreshToken.getExpiryDate());

        return refreshToken;

    }

    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new sign in request");
        }

        return token;
    }

    @Transactional
    public int deleteByUserName(String username) {
        return refreshTokenRepository.deleteByUserAccount(accountRepository.findAccountByUsername(username).get());
    }

    public TokenRefreshResponseDto refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));

        refreshToken = verifyExpiration(refreshToken);

        refreshTokenRepository.delete(refreshToken);
        RefreshToken newRefreshToken = createRefreshToken(
                refreshToken.getUserAccount().getUsername(),
                refreshToken.getExpiryDate()
        );

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(refreshToken.getUserAccount().getUsername());
        String token = jwtUtils.generateToken(userDetails);

        return new TokenRefreshResponseDto(token, newRefreshToken.getToken());
    }
}
