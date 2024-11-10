package ru.clevertec.userservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.userservice.dto.response.JwtResponse;
import ru.clevertec.userservice.model.Role;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.security.jwt.JwtTokenProvider;
import ru.clevertec.userservice.security.props.JwtProperties;
import ru.clevertec.userservice.service.UserIdentityService;
import ru.clevertec.userservice.service.UserService;
import ru.clevertec.userservice.util.testdata.UserTestData;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static ru.clevertec.userservice.util.Constants.ROLE_JOURNALIST;
import static ru.clevertec.userservice.util.initdata.InitData.ACCESS_TEST;
import static ru.clevertec.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static ru.clevertec.userservice.util.initdata.InitData.ID_JOURNALIST;
import static ru.clevertec.userservice.util.initdata.InitData.REFRESH_TEST;
import static ru.clevertec.userservice.util.initdata.InitData.SECRET_TEST;

@ExtendWith(SpringExtension.class)
class JwtTokenProviderTest {

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserIdentityService userIdentityService;

    @Mock
    private UserService userService;

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    private Key key;

    @BeforeEach
    public void setup() {
        when(jwtProperties.getSecret()).thenReturn(SECRET_TEST);
        when(jwtProperties.getAccess()).thenReturn(ACCESS_TEST);
        when(jwtProperties.getRefresh()).thenReturn(REFRESH_TEST);
        jwtTokenProvider.init();
        key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    @Test
    void shouldReturnExpectedCreatedAccessToken() {
        // given
        Role role = new Role();
        role.setName(ROLE_JOURNALIST);

        // when
        String token = jwtTokenProvider.createAccessToken(ID_JOURNALIST, EMAIL_JOURNALIST, List.of(role));

        // then
        assertNotNull(token);
        assertThat(token).isInstanceOf(String.class);
    }

    @Test
    void shouldReturnExpectedCreatedRefreshToken() {
        // given
        Role role = new Role();
        role.setName(ROLE_JOURNALIST);

        // when
        String token = jwtTokenProvider.createRefreshToken(ID_JOURNALIST, EMAIL_JOURNALIST);

        // then
        assertNotNull(token);
        assertThat(token).isInstanceOf(String.class);
    }

    @Test
    void shouldReturnExpectedRefreshUserToken() {
        // given
        UUID userId = ID_JOURNALIST;
        String userEmail = EMAIL_JOURNALIST;
        User user = UserTestData.getJournalist();
        when(userIdentityService.getUserOrThrowException(userId)).
                thenReturn(user);

        // when
        String refreshToken = jwtTokenProvider.createRefreshToken(userId, userEmail);
        JwtResponse jwtResponse = jwtTokenProvider.refreshUserToken(refreshToken);

        // then
        assertThat(jwtResponse)
                .isNotNull();
    }

    @Test
    void shouldReturnThrowExceptionWhenTokenIsExpired() {
        // given
        Role role = new Role();
        role.setName(ROLE_JOURNALIST);
        Claims claims = Jwts.claims().setSubject(EMAIL_JOURNALIST);
        claims.put("id", ID_JOURNALIST);
        claims.put("roles", Collections.singletonList(role.getName()));
        Instant validity = Instant.now().minus(1, ChronoUnit.HOURS);
        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(key)
                .compact();

        // when, then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(expiredToken))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void shouldReturnTrueWhenTokenIsValid() {
        // given
        UUID userId = ID_JOURNALIST;
        String userEmail = EMAIL_JOURNALIST;
        String token = jwtTokenProvider.createRefreshToken(userId, userEmail);

        // when
        boolean actual = jwtTokenProvider.validateToken(token);

        assertThat(actual).isTrue();
    }

    @Test
    @SneakyThrows
    void shouldReturnExpectedEmailWhenTokenIsValid() {
        // given
        UUID userId = ID_JOURNALIST;
        String expectedUsername = EMAIL_JOURNALIST;
        Claims claims = Jwts.claims().setSubject(expectedUsername);
        claims.put("id", userId);
        Instant validity = Instant.now()
                .plus(jwtProperties.getRefresh(), ChronoUnit.DAYS);
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(key)
                .compact();

        // when
        String actualUsername = jwtTokenProvider.getUsername(token);

        // then
        assertThat(actualUsername).isEqualTo(expectedUsername);
    }
}