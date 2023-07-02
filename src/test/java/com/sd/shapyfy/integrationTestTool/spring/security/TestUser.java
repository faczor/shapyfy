package com.sd.shapyfy.integrationTestTool.spring.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value(staticConstructor = "of")
public class TestUser {

    String id;

    @RequiredArgsConstructor
    public enum PredefinedUsers {
        DEFAULT(TestUser.of("42141241241242141"));

        @Getter
        private final TestUser testUser;
    }
}
