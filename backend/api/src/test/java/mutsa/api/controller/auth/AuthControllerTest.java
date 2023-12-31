package mutsa.api.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import mutsa.api.ApiApplication;
import mutsa.api.config.TestBootstrapConfig;
import mutsa.api.config.TestRedisConfiguration;
import mutsa.api.dto.auth.LoginRequest;
import mutsa.api.util.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(classes = {ApiApplication.class, TestRedisConfiguration.class, TestBootstrapConfig.class})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginTest() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin1234");
        String body = new ObjectMapper().writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login", body)
                        .cookie(new Cookie(JwtTokenProvider.REFRESH_TOKEN, "value"))
                        .contentType("application/json")
                        .content(body)
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}