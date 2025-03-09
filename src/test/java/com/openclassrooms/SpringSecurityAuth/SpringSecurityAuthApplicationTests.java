package com.openclassrooms.SpringSecurityAuth;

import com.openclassrooms.SpringSecurityAuth.controllers.LoginController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class SpringSecurityAuthApplicationTests {

	@Autowired
	private LoginController loginController;

	@Test
	void contextLoads() throws Exception {
		assertThat(loginController).isNotNull();
	}

}
