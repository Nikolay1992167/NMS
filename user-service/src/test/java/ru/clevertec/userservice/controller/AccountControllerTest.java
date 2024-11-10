package ru.clevertec.userservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.userservice.security.jwt.JwtTokenProvider;
import ru.clevertec.userservice.util.IntegrationTest;
import ru.clevertec.userservice.util.PostgresSqlContainerInitializer;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.userservice.util.initdata.InitData.BEARER;
import static ru.clevertec.userservice.util.initdata.InitData.EMAIL_JOURNALIST_FOR_IT;
import static ru.clevertec.userservice.util.initdata.InitData.ID_JOURNALIST_FOR_IT;
import static ru.clevertec.userservice.util.initdata.InitData.URL_USERS;

@IntegrationTest
@AutoConfigureMockMvc
class AccountControllerTest extends PostgresSqlContainerInitializer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void shouldReturnExpectedValueAndStatus200() throws Exception {
        // given
        String userToken = jwtTokenProvider.createRefreshToken(ID_JOURNALIST_FOR_IT, EMAIL_JOURNALIST_FOR_IT);

        // when, then
        mockMvc.perform(get(URL_USERS + "/details")
                        .header(AUTHORIZATION, BEARER + userToken)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}