package com.elearning.platform;

import com.elearning.platform.security.PasswordConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"jwt.secret=test-secret"})
@ContextConfiguration(classes = {PasswordConfiguration.class})
class PlatformApplicationTests {

    @Test
    void contextLoads() {
    }

}
