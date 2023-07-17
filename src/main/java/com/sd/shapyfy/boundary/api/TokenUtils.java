package com.sd.shapyfy.boundary.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sd.shapyfy.domain.model.UserId;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Base64.Decoder;

import static java.util.Base64.getUrlDecoder;

@Slf4j
public class TokenUtils {

    private static final Decoder URL_DECODER = getUrlDecoder();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static UserId currentUserId() {
        try {
            String authorization = extractTokenFromHeader();
            JwtDetails jwtDetails = buildJwtDetails(authorization);

            return UserId.of(jwtDetails.getUserId());
        } catch (NullPointerException e) {
            throw new UnauthorizedRestCallException();
        }
    }

    @SneakyThrows(JsonProcessingException.class)
    private static JwtDetails buildJwtDetails(String authorization) {
        String bearerValue = authorization.replace("Bearer ", "");
        String userDetailsToken = bearerValue.split("\\.")[1];

        return OBJECT_MAPPER.readValue(new String(URL_DECODER.decode(userDetailsToken)), JwtDetails.class);
    }

    private static String extractTokenFromHeader() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class JwtDetails {

        @JsonProperty(value = "user_id")
        String userId;
    }

}
