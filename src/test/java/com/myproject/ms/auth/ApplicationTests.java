package com.myproject.ms.auth;

import com.myproject.ms.auth.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(SecurityConfig.class)
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
