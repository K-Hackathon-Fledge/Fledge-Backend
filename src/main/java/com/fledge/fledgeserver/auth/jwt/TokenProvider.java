package com.fledge.fledgeserver.auth.jwt;

import com.fledge.fledgeserver.auth.dto.TokenResponse;
import com.fledge.fledgeserver.auth.service.CustomUserDetailsService;
import com.fledge.fledgeserver.exception.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.crypto.SecretKey;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.fledge.fledgeserver.auth.jwt.JwtConstants.*;
import static com.fledge.fledgeserver.exception.ErrorCode.INVALID_TOKEN;

@Component
@Slf4j
public class TokenProvider {

    private final CustomUserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;
    private final long accessExpired;
    private final long refreshExpired;
    private SecretKey key;

    private String secret;

    public TokenProvider(
            CustomUserDetailsService userDetailsService, @Value("${jwt.key}") String secret,
            RedisTemplate<String, String> redisTemplate,
            @Value("${jwt.access_expired-time}") long accessExpired,
            @Value("${jwt.refresh_expired-time}") long refreshExpired) {
        this.userDetailsService = userDetailsService;
        this.secret = secret;
        this.redisTemplate = redisTemplate;
        this.accessExpired = accessExpired;
        this.refreshExpired = refreshExpired;
    }

    @PostConstruct
    public void afterPropertiesSet() {
        byte[] decoded = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(decoded);
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            token = token.substring(BEARER_PREFIX.length());
        }

        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new CustomException(INVALID_TOKEN, "Invalid token " + e);
        } catch (ExpiredJwtException e) {
            throw new CustomException(INVALID_TOKEN, "Expired token " + e);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(INVALID_TOKEN, "Token not supported " + e);
        } catch (IllegalArgumentException e) {
            throw new CustomException(INVALID_TOKEN, "Invalid token " + e);
        }
    }

    public TokenResponse createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);

        Date accessExpiration = Date.from(issuedAt.plus(accessExpired, ChronoUnit.SECONDS));
        Date refreshExpiration = Date.from(issuedAt.plus(refreshExpired, ChronoUnit.SECONDS));

        var accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("Fledge")
                .setIssuedAt(new Date())
                .setExpiration(accessExpiration)
                .setSubject(authentication.getName())
                .signWith(key, SignatureAlgorithm.HS512)
                .claim(AUTHORITIES, authorities)
                .compact();

        var refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("Fledge")
                .setIssuedAt(new Date())
                .setExpiration(refreshExpiration)
                .setSubject(authentication.getName())
                .signWith(key, SignatureAlgorithm.HS512)
                .claim(AUTHORITIES, authorities)
                .compact();

        updateUserAndStoreRefreshToken(authentication.getName(), refreshToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    private void updateUserAndStoreRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue().set(username, refreshToken, refreshExpired, TimeUnit.SECONDS);
    }

    public Authentication resolveToken(String token) {

        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<SimpleGrantedAuthority> authorities = Stream.of(
                        String.valueOf(claims.get(AUTHORITIES)).split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails userDetails = userDetailsService.loadUserByUsername(
                claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

}
