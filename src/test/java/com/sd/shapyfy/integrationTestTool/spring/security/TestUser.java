package com.sd.shapyfy.integrationTestTool.spring.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value(staticConstructor = "of")
public class TestUser {

    String id;

    @RequiredArgsConstructor
    public enum PredefinedUsers {

        NEW_USER(TestUser.of("00000000000000000")),
        USER_WITH_INITIALIZED_EMPTY_TRAINING(TestUser.of("00000000000000001")),
        USER_WITH_DRAFT_TRAINING(TestUser.of("00000000000000002")),
        USER_WITH_ACTIVE_TRAINING(TestUser.of("00000000000000003")),
        USER_WITH_RUNNING_TRAINING(TestUser.of("00000000000000004")),
        USER_WITH_RUNNING_TRAINING_READY_TO_FINISH(TestUser.of("00000000000000005")),
        ;

        @Getter
        private final TestUser testUser;
    }
}
