package ru.clevertec.IT.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.dto.request.CreateNewsDto;
import ru.clevertec.dto.request.CreateNewsDtoJournalist;
import ru.clevertec.dto.request.Filter;
import ru.clevertec.enams.ErrorMessage;
import ru.clevertec.util.IntegrationTest;
import ru.clevertec.util.JwtTokenTestUtils;
import ru.clevertec.util.PostgresqlTestContainer;
import ru.clevertec.util.testdata.NewsTestData;
import ru.clevertec.util.testdata.UserTestData;

import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.util.init.InitData.BEARER;
import static ru.clevertec.util.init.InitData.EMAIL_ADMIN_FOR_IT;
import static ru.clevertec.util.init.InitData.EMAIL_JOURNALIST_FOR_IT;
import static ru.clevertec.util.init.InitData.EMAIL_SUBSCRIBER_FOR_IT;
import static ru.clevertec.util.init.InitData.ID_ADMIN_FOR_IT;
import static ru.clevertec.util.init.InitData.ID_JOURNALIST_FOR_IT;
import static ru.clevertec.util.init.InitData.ID_NEWS;
import static ru.clevertec.util.init.InitData.ID_NEWS_FOR_IT;
import static ru.clevertec.util.init.InitData.ID_NEWS_FOR_IT_DELETE;
import static ru.clevertec.util.init.InitData.ID_NEWS_FOR_IT_JOURNALIST;
import static ru.clevertec.util.init.InitData.ID_NOT_EXIST;
import static ru.clevertec.util.init.InitData.ID_SUBSCRIBER_FOR_IT;
import static ru.clevertec.util.init.InitData.URL_DATA_BY_ID_USER_AND_TOKEN;
import static ru.clevertec.util.init.InitData.URL_DATA_BY_TOKEN;
import static ru.clevertec.util.init.InitData.URL_NEWS;

@IntegrationTest
@AutoConfigureMockMvc
@WireMockTest(httpPort = 8081)
@Sql("classpath:sql/insert_data_for_IT.sql")
public class NewsControllerTest extends PostgresqlTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String tokenAdmin;
    private String tokenJournalist;
    private String tokenSubscriber;

    @BeforeEach
    void setUp() {
        tokenAdmin = JwtTokenTestUtils.generateToken(EMAIL_ADMIN_FOR_IT, ID_ADMIN_FOR_IT, List.of("ADMIN"));
        tokenJournalist = JwtTokenTestUtils.generateToken(EMAIL_JOURNALIST_FOR_IT, ID_JOURNALIST_FOR_IT, List.of("JOURNALIST"));
        tokenSubscriber = JwtTokenTestUtils.generateToken(EMAIL_SUBSCRIBER_FOR_IT, ID_SUBSCRIBER_FOR_IT, List.of("SUBSCRIBER"));
    }

    @Nested
    class FindNewsByIdGetEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            UUID newsId = ID_NEWS_FOR_IT;

            // when, then
            mockMvc.perform(get(URL_NEWS + "/" + newsId)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(newsId.toString()));
        }

        @Test
        void shouldThrowExceptionAndStatus404() throws Exception {
            // given
            UUID newsId = ID_NOT_EXIST;

            // when, then
            mockMvc.perform(get(URL_NEWS + "/" + newsId)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error_message")
                            .value(ErrorMessage.NEWS_NOT_FOUND.getMessage() + newsId));
        }
    }

    @Nested
    class GetAllNewsGetEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            MvcResult mvcResult = mockMvc.perform(get(URL_NEWS)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            JSONObject jsonObject = new JSONObject(response.getContentAsString());
            assertThat(jsonObject.get("totalPages")).isEqualTo(1);
            assertThat(jsonObject.get("totalElements")).isEqualTo(3);
            assertThat(jsonObject.get("number")).isEqualTo(0);
        }
    }

    @Nested
    class FindNewsByFilterGetEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            Filter filter = Filter.builder()
                    .part("is")
                    .build();
            String json = objectMapper.writeValueAsString(filter);
            MvcResult mvcResult = mockMvc.perform(get(URL_NEWS + "/filter")
                            .contentType(APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            JSONObject jsonObject = new JSONObject(response.getContentAsString());
            assertThat(jsonObject.get("totalPages")).isEqualTo(1);
            assertThat(jsonObject.get("totalElements")).isEqualTo(2);
            assertThat(jsonObject.get("number")).isEqualTo(0);
        }
    }

    @Nested
    @WireMockTest(httpPort = 8081)
    class CreateNewsAdminPostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus201() throws Exception {
            // given
            CreateNewsDto createNewsDto = NewsTestData.getCreateNewsDtoITForAdmin();
            String json = objectMapper.writeValueAsString(createNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseAdmin());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_ID_USER_AND_TOKEN + createNewsDto.idAuthor()))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(post(URL_NEWS + "/admin")
                            .header(AUTHORIZATION, BEARER + tokenAdmin)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isCreated());
        }

        @Test
        void shouldThrowExceptionAndStatus401() throws Exception {
            // given
            CreateNewsDto createNewsDto = NewsTestData.getCreateNewsDtoITForAdmin();
            String json = objectMapper.writeValueAsString(createNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseAdmin());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            // when, then
            mockMvc.perform(post(URL_NEWS + "/admin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isUnauthorized());
        }

        @Test
        void shouldThrowExceptionAndStatus403() throws Exception {
            // given
            CreateNewsDto createNewsDto = NewsTestData.getCreateNewsDtoITForAdmin();
            String json = objectMapper.writeValueAsString(createNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseSubscriber());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            // when, then
            mockMvc.perform(post(URL_NEWS + "/admin")
                            .header(AUTHORIZATION, BEARER + tokenSubscriber)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isForbidden());
        }

        @Test
        void shouldThrowExceptionAndStatus400WhenInvalidRequestData() throws Exception {
            // given
            CreateNewsDto createNewsDto = NewsTestData.getCreateNewsDtoIncorrect();
            String json = objectMapper.writeValueAsString(createNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseAdmin());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            // when, then
            mockMvc.perform(post(URL_NEWS + "/admin")
                            .header(AUTHORIZATION, BEARER + tokenAdmin)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isBadRequest());
        }
    }

    @Nested
    @WireMockTest(httpPort = 8081)
    class CreateNewsJournalistPostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus201() throws Exception {
            // given
            CreateNewsDtoJournalist createNewsDto = NewsTestData.getCreateNewsDtoJournalist();
            String json = objectMapper.writeValueAsString(createNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseJournalist());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            // when, then
            mockMvc.perform(post(URL_NEWS + "/journalist")
                            .header(AUTHORIZATION, BEARER + tokenJournalist)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isCreated());
        }

        @Test
        void shouldThrowExceptionAndStatus401() throws Exception {
            // given
            CreateNewsDtoJournalist createNewsDto = NewsTestData.getCreateNewsDtoJournalist();
            String json = objectMapper.writeValueAsString(createNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseJournalist());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            // when, then
            mockMvc.perform(post(URL_NEWS + "/journalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isUnauthorized());
        }

        @Test
        void shouldThrowExceptionAndStatus403() throws Exception {
            // given
            CreateNewsDtoJournalist createNewsDto = NewsTestData.getCreateNewsDtoJournalist();
            String json = objectMapper.writeValueAsString(createNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseSubscriber());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            // when, then
            mockMvc.perform(post(URL_NEWS + "/journalist")
                            .header(AUTHORIZATION, BEARER + tokenSubscriber)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isForbidden());
            ;
        }

        @Test
        void shouldThrowExceptionAndStatus400WhenInvalidRequestData() throws Exception {
            // given
            CreateNewsDtoJournalist createNewsDto = NewsTestData.getCreateNewsDtoJournalistIncorrect();
            String json = objectMapper.writeValueAsString(createNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseJournalist());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            // when, then
            mockMvc.perform(post(URL_NEWS + "/journalist")
                            .header(AUTHORIZATION, BEARER + tokenJournalist)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isBadRequest());
        }
    }

    @Nested
    @WireMockTest(httpPort = 8081)
    class UpdateNewsAdminPutEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            UUID newsId = ID_NEWS_FOR_IT;
            CreateNewsDto updateNewsDto = NewsTestData.getCreateNewsDto();
            String json = objectMapper.writeValueAsString(updateNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseAdmin());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_ID_USER_AND_TOKEN + updateNewsDto.idAuthor()))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(put(URL_NEWS + "/admin/" + newsId)
                            .header(AUTHORIZATION, BEARER + tokenAdmin)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.id").value(newsId.toString()),
                            jsonPath("$.text").value(updateNewsDto.text()));
        }

        @Test
        void shouldThrowExceptionAndStatus401() throws Exception {
            // given
            UUID newsId = ID_NEWS_FOR_IT;
            CreateNewsDto updateNewsDto = NewsTestData.getCreateNewsDto();
            String json = objectMapper.writeValueAsString(updateNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseAdmin());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_ID_USER_AND_TOKEN + updateNewsDto.idAuthor()))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(put(URL_NEWS + "/admin/" + newsId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isUnauthorized());
        }

        @Test
        void shouldThrowExceptionAndStatus403() throws Exception {
            // given
            UUID newsId = ID_NEWS_FOR_IT;
            CreateNewsDto updateNewsDto = NewsTestData.getCreateNewsDto();
            String json = objectMapper.writeValueAsString(updateNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseSubscriber());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_ID_USER_AND_TOKEN + updateNewsDto.idAuthor()))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(put(URL_NEWS + "/admin/" + newsId)
                            .header(AUTHORIZATION, BEARER + tokenSubscriber)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isForbidden());
        }

        @Test
        void shouldThrowExceptionAndStatus404WhenNewsNotFound() throws Exception {
            // given
            UUID newsId = ID_NEWS;
            CreateNewsDto updateNewsDto = NewsTestData.getCreateNewsDto();
            String json = objectMapper.writeValueAsString(updateNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseAdmin());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_ID_USER_AND_TOKEN + updateNewsDto.idAuthor()))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(put(URL_NEWS + "/admin/" + newsId)
                            .header(AUTHORIZATION, BEARER + tokenAdmin)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.error_message")
                                    .value(ErrorMessage.NEWS_NOT_FOUND.getMessage() + newsId));
        }

        @Test
        void shouldThrowExceptionAndStatus400WhenInvalidRequestData() throws Exception {
            // given
            UUID newsId = ID_NEWS_FOR_IT;
            CreateNewsDto updateNewsDto = NewsTestData.getCreateNewsDtoIncorrect();
            String json = objectMapper.writeValueAsString(updateNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseAdmin());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_ID_USER_AND_TOKEN + updateNewsDto.idAuthor()))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(put(URL_NEWS + "/admin/" + newsId)
                            .header(AUTHORIZATION, BEARER + tokenAdmin)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isBadRequest());
        }
    }

    @Nested
    @Transactional
    @WireMockTest(httpPort = 8081)
    class UpdateNewsJournalistPutEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            UUID newsId = ID_NEWS_FOR_IT_JOURNALIST;
            CreateNewsDtoJournalist updateNewsDto = NewsTestData.getCreateNewsDtoJournalist();
            String json = objectMapper.writeValueAsString(updateNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseJournalist());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(put(URL_NEWS + "/journalist/" + newsId)
                            .header(AUTHORIZATION, BEARER + tokenJournalist)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.id").value(newsId.toString()),
                            jsonPath("$.text").value(updateNewsDto.text()));
        }

        @Test
        void shouldThrowExceptionAndStatus401() throws Exception {
            // given
            UUID newsId = ID_NEWS_FOR_IT_JOURNALIST;
            CreateNewsDtoJournalist updateNewsDto = NewsTestData.getCreateNewsDtoJournalist();
            String json = objectMapper.writeValueAsString(updateNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseJournalist());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(put(URL_NEWS + "/journalist/" + newsId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isUnauthorized());
        }

        @Test
        void shouldThrowExceptionAndStatus403() throws Exception {
            // given
            UUID newsId = ID_NEWS_FOR_IT_JOURNALIST;
            CreateNewsDtoJournalist updateNewsDto = NewsTestData.getCreateNewsDtoJournalist();
            String json = objectMapper.writeValueAsString(updateNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseSubscriber());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(put(URL_NEWS + "/journalist/" + newsId)
                            .header(AUTHORIZATION, BEARER + tokenSubscriber)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isForbidden());
        }

        @Test
        void shouldThrowExceptionAndStatus404WhenNewsNotFound() throws Exception {
            // given
            UUID newsId = ID_NEWS;
            CreateNewsDtoJournalist updateNewsDto = NewsTestData.getCreateNewsDtoJournalist();
            String json = objectMapper.writeValueAsString(updateNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseJournalist());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(put(URL_NEWS + "/journalist/" + newsId)
                            .header(AUTHORIZATION, BEARER + tokenJournalist)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.error_message")
                                    .value(ErrorMessage.NEWS_NOT_FOUND.getMessage() + newsId));
        }

        @Test
        void shouldThrowExceptionAndStatus400WhenInvalidRequestData() throws Exception {
            // given
            UUID newsId = ID_NEWS_FOR_IT_JOURNALIST;
            CreateNewsDtoJournalist updateNewsDto = NewsTestData.getCreateNewsDtoJournalistIncorrect();
            String json = objectMapper.writeValueAsString(updateNewsDto);
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseJournalist());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(put(URL_NEWS + "/journalist/" + newsId)
                            .header(AUTHORIZATION, BEARER + tokenJournalist)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpectAll(
                            status().isBadRequest());
        }
    }

    @Nested
    @Transactional
    class DeleteNewsByIdDeleteEndPointTest {

        @Test
        void shouldDeleteCommentAndStatus200() throws Exception {
            // given
            UUID newsId = ID_NEWS_FOR_IT_DELETE;
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseAdmin());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(delete(URL_NEWS + "/" + newsId)
                            .header(AUTHORIZATION, BEARER + tokenAdmin)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isOk());
        }

        @Test
        void shouldThrowExceptionAndStatus401() throws Exception {
            // given
            UUID newsId = ID_NEWS_FOR_IT_DELETE;
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseAdmin());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(delete(URL_NEWS + "/" + newsId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isUnauthorized());
        }

        @Test
        void shouldThrowExceptionAndStatus403() throws Exception {
            // given
            UUID newsId = ID_NEWS_FOR_IT;
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseJournalist());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(delete(URL_NEWS + "/" + newsId)
                            .header(AUTHORIZATION, BEARER + tokenJournalist)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isForbidden());
        }

        @Test
        void shouldThrowExceptionAndStatus403WhenUserNotRightChangeData() throws Exception {
            // given
            UUID newsId = ID_NEWS_FOR_IT_DELETE;
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseJournalist());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(delete(URL_NEWS + "/" + newsId)
                            .header(AUTHORIZATION, BEARER + tokenJournalist)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isForbidden());
        }

        @Test
        void shouldThrowExceptionAndStatus404WhenCommentNotFound() throws Exception {
            // given
            UUID newsId = ID_NEWS;
            String userResponseJson = objectMapper.writeValueAsString(UserTestData.getUserResponseAdmin());

            stubFor(WireMock.get(urlEqualTo(URL_DATA_BY_TOKEN))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(userResponseJson)
                            .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

            // when, then
            mockMvc.perform(delete(URL_NEWS + "/" + newsId)
                            .header(AUTHORIZATION, BEARER + tokenAdmin)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.error_message")
                                    .value(ErrorMessage.NEWS_NOT_FOUND.getMessage() + newsId));
        }
    }
}