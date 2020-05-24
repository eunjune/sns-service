package com.github.prgrms.social.api;

import com.github.prgrms.social.api.controller.ProfileController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockExpressionEvaluator;

import static org.junit.jupiter.api.Assertions.*;

class ProfileControllerTest {

    @Test
    @DisplayName("prod profile 조회")
    public void prod_profile() {
        String expectedProfile = "prod";
        MockEnvironment environment = new MockEnvironment();
        environment.addActiveProfile(expectedProfile);
        environment.addActiveProfile("other1");
        environment.addActiveProfile("other2");

        ProfileController profileController = new ProfileController(environment);

        String profile = profileController.profile();

        assertEquals(profile, expectedProfile);
    }

    @Test
    @DisplayName("active profile이 없을 때")
    public void no_active_profile() {
        String expectedProfile = "default";
        MockEnvironment environment = new MockEnvironment();

        ProfileController profileController = new ProfileController(environment);

        String profile = profileController.profile();

        assertEquals(profile, expectedProfile);
    }

}
