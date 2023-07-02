package com.sd.shapyfy.integrationTestTool.spring.security;

import com.sd.shapyfy.integrationTestTool.spring.security.TestUser;

public interface TestTokenProvider {

    String provideFor(TestUser user);
}
