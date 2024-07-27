package com.fledge.fledgeserver.auth.jwt;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value = "jwt", timeToLive = 60 * 60 * 24 * 7)
public class Token {

    @Id
    private String id;

    private String refreshToken;

    @Indexed
    private String accessToken;

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
