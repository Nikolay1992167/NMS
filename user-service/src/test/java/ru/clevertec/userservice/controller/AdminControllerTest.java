package ru.clevertec.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.userservice.dto.request.UserUpdateRequest;
import ru.clevertec.userservice.enums.error_response.ErrorMessage;
import ru.clevertec.userservice.security.jwt.JwtTokenProvider;
import ru.clevertec.userservice.util.IntegrationTest;
import ru.clevertec.userservice.util.PostgresSqlContainerInitializer;
import ru.clevertec.userservice.util.testdata.UserTestData;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.userservice.util.initdata.InitData.BEARER;
import static ru.clevertec.userservice.util.initdata.InitData.EMAIL_ADMIN_FOR_IT;
import static ru.clevertec.userservice.util.initdata.InitData.ID_ADMIN;
import static ru.clevertec.userservice.util.initdata.InitData.ID_ADMIN_FOR_IT;
import static ru.clevertec.userservice.util.initdata.InitData.ID_JOURNALIST_FOR_IT;
import static ru.clevertec.userservice.util.initdata.InitData.ID_NOT_EXIST;
import static ru.clevertec.userservice.util.initdata.InitData.ID_SUBSCRIBER_FOR_IT;
import static ru.clevertec.userservice.util.initdata.InitData.URL_ADMIN;

@IntegrationTest
@AutoConfigureMockMvc
class AdminControllerTest extends PostgresSqlContainerInitializer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String ADMIN_TOKEN;

    @BeforeEach
    void setUp() {
        ADMIN_TOKEN = jwtTokenProvider.createRefreshToken(ID_ADMIN_FOR_IT, EMAIL_ADMIN_FOR_IT);
    }

    @Nested
    class FindAllGetEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            MvcResult mvcResult = mockMvc.perform(get(URL_ADMIN)
                            .header(AUTHORIZATION, BEARER + ADMIN_TOKEN)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            JSONObject jsonObject = new JSONObject(response.getContentAsString());
            assertThat(jsonObject.get("totalPages")).isEqualTo(1);
            assertThat(jsonObject.get("totalElements")).isEqualTo(4);
            assertThat(jsonObject.get("number")).isEqualTo(0);
            assertThat(jsonObject.get("content")).isNotNull();
        }
    }

    @Nested
    class FindByIdGetEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            UUID userId = ID_JOURNALIST_FOR_IT;

            // when, then
            mockMvc.perform(get(URL_ADMIN + "/" + userId)
                            .header(AUTHORIZATION, BEARER + ADMIN_TOKEN)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(userId.toString()));
        }

        @Test
        void shouldReturnThrowExceptionAndStatus401() throws Exception {
            mockMvc.perform(get(URL_ADMIN + "/" + ID_JOURNALIST_FOR_IT))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void shouldReturnThrowExceptionAndStatus404() throws Exception {
            mockMvc.perform(get(URL_ADMIN + "/" + ID_NOT_EXIST)
                            .header(AUTHORIZATION, BEARER + ADMIN_TOKEN)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error_message")
                            .value(ErrorMessage.USER_NOT_FOUND.getMessage() + ID_NOT_EXIST));
        }
    }

    @Nested
    @Transactional
    class UpdatePutEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            UUID userId = ID_JOURNALIST_FOR_IT;
            UserUpdateRequest updateRequest = UserTestData.getUserUpdateRequest();
            String json = objectMapper.writeValueAsString(updateRequest);

            // when, then
            mockMvc.perform(put(URL_ADMIN + "/" + userId)
                            .header(AUTHORIZATION, BEARER + ADMIN_TOKEN)
                            .contentType(APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpectAll(jsonPath("$.id").value(userId.toString()),
                            jsonPath("$.firstName").value(updateRequest.getFirstName()),
                            jsonPath("$.lastName").value(updateRequest.getLastName()),
                            jsonPath("$.email").value(updateRequest.getEmail()));
        }
    }

    @Nested
    @Transactional
    class DeactivateUserPathEndpointTest {

        @Test
        void shouldDeactivateUser() throws Exception {
            mockMvc.perform(patch(URL_ADMIN + "/deactivate/{id}", ID_JOURNALIST_FOR_IT)
                            .header(AUTHORIZATION, BEARER + ADMIN_TOKEN)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldThrowExceptionWhenDeactivateAdminUser() throws Exception {
            mockMvc.perform(patch(URL_ADMIN + "/deactivate/" + ID_ADMIN)
                            .header(AUTHORIZATION, BEARER + ADMIN_TOKEN)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message")
                            .value(ErrorMessage.CHANGE_STATUS.getMessage()));
        }
    }

    @Nested
    @Transactional
    class DeleteUserPathEndpointTest {

        @Test
        void shouldDeleteUser() throws Exception {
            mockMvc.perform(delete(URL_ADMIN + "/delete/" + ID_SUBSCRIBER_FOR_IT)
                            .header(AUTHORIZATION, BEARER + ADMIN_TOKEN)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldThrowExceptionWhenDeactivateAdminUser() throws Exception {
            mockMvc.perform(delete(URL_ADMIN + "/delete/" + ID_ADMIN)
                            .header(AUTHORIZATION, BEARER + ADMIN_TOKEN)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message")
                            .value(ErrorMessage.CHANGE_STATUS.getMessage()));
        }
    }
}