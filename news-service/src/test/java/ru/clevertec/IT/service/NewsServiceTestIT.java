package ru.clevertec.IT.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import ru.clevertec.dto.request.CreateNewsDto;
import ru.clevertec.dto.request.CreateNewsDtoJournalist;
import ru.clevertec.dto.request.Filter;
import ru.clevertec.dto.response.ResponseNews;
import ru.clevertec.dto.response.UserResponse;
import ru.clevertec.enams.ErrorMessage;
import ru.clevertec.exception.handling.exception.NotFoundException;
import ru.clevertec.service.NewsService;
import ru.clevertec.util.IntegrationTest;
import ru.clevertec.util.JwtTokenTestUtils;
import ru.clevertec.util.PostgresqlTestContainer;
import ru.clevertec.util.testdata.JwtUserTestData;
import ru.clevertec.util.testdata.NewsTestData;
import ru.clevertec.util.testdata.UserTestData;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static ru.clevertec.util.init.InitData.BEARER;
import static ru.clevertec.util.init.InitData.DEFAULT_PAGE_REQUEST_FOR_IT;
import static ru.clevertec.util.init.InitData.EMAIL_ADMIN_FOR_IT;
import static ru.clevertec.util.init.InitData.ID_ADMIN_FOR_IT;
import static ru.clevertec.util.init.InitData.ID_AUTHOR_NEWS;
import static ru.clevertec.util.init.InitData.ID_NEWS;
import static ru.clevertec.util.init.InitData.ID_NEWS_FOR_IT;
import static ru.clevertec.util.init.InitData.ID_NEWS_FOR_IT_DELETE;
import static ru.clevertec.util.init.InitData.ID_NEWS_FOR_IT_JOURNALIST;

@IntegrationTest
@WireMockTest(httpPort = 8081)
@Sql("classpath:sql/insert_data_for_IT.sql")
public class NewsServiceTestIT extends PostgresqlTestContainer {

    @Autowired
    private NewsService newsService;

    @Autowired
    private ObjectMapper objectMapper;

    private String tokenAdmin;

    @BeforeEach
    void setUp() {
        tokenAdmin = JwtTokenTestUtils.generateToken(EMAIL_ADMIN_FOR_IT, ID_ADMIN_FOR_IT, List.of("ADMIN"));
    }

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Test
    void shouldReturnExpectedNews() {
        // given, when
        ResponseNews actual = newsService.findNewsById(ID_NEWS_FOR_IT);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.id()).isEqualTo(ID_NEWS_FOR_IT);
    }

    @Test
    void shouldTrowExceptionWhenNewsNotFound() {
        // given, when, then
        assertThatThrownBy(() -> newsService.findNewsById(ID_NEWS))
                .isExactlyInstanceOf(NotFoundException.class)
                .hasMessageContaining(ErrorMessage.NEWS_NOT_FOUND.getMessage() + ID_NEWS);
    }

    @Test
    void shouldReturnAllNews() {
        // given, when
        Page<ResponseNews> actual = newsService.getAllNews(DEFAULT_PAGE_REQUEST_FOR_IT);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).hasSize(3);
    }

    @Test
    void shouldReturnNewsByFilter() {
        // given
        Filter filter = Filter.builder()
                .part("is")
                .build();

        // when
        Page<ResponseNews> actual = newsService.findNewsByFilter(filter, DEFAULT_PAGE_REQUEST_FOR_IT);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).hasSize(2);
    }

    @Test
    void shouldReturnCreatedNewsFromAdmin() throws JsonProcessingException {
        // given
        CreateNewsDto createNewsDto = NewsTestData.getCreateNewsDtoITForAdmin();
        UserResponse userResponseAdmin = UserTestData.getUserResponseAdmin();
        String json = objectMapper.writeValueAsString(userResponseAdmin);

        when(authentication.getPrincipal())
                .thenReturn(JwtUserTestData.getJwtUser());
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        stubFor(WireMock.get(urlEqualTo("/api/v1/admin/" + userResponseAdmin.id()))
                .willReturn(aResponse()
                        .withHeader(AUTHORIZATION, BEARER + tokenAdmin)
                        .withStatus(200)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

        // when
        ResponseNews actual = newsService.createNewsAdmin(createNewsDto, tokenAdmin);

        // then
        assertThat(actual.title()).isEqualTo(createNewsDto.title());
        assertThat(actual.text()).isEqualTo(createNewsDto.text());
    }

    @Test
    void shouldReturnCreatedNewsFromJournalist() {
        // given
        CreateNewsDtoJournalist createNewsDto = NewsTestData.getCreateNewsDtoJournalist();

        when(authentication.getPrincipal())
                .thenReturn(JwtUserTestData.getJwtUserValidationNews());
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // when
        ResponseNews actual = newsService.createNewsJournalist(createNewsDto);

        // then
        assertThat(actual.title()).isEqualTo(createNewsDto.title());
        assertThat(actual.text()).isEqualTo(createNewsDto.text());
    }

    @Test
    void shouldReturnUpdatedNewsFromAdmin() throws JsonProcessingException {
        // given
        CreateNewsDto updateNewsDto = NewsTestData.getCreateNewsDto();
        UserResponse userResponseAdmin = UserTestData.getUserResponseAdmin();
        String json = objectMapper.writeValueAsString(userResponseAdmin);

        when(authentication.getPrincipal())
                .thenReturn(JwtUserTestData.getJwtUserAdmin());
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        stubFor(WireMock.get(urlEqualTo("/api/v1/admin/" + ID_AUTHOR_NEWS))
                .willReturn(aResponse()
                        .withHeader(AUTHORIZATION, BEARER + tokenAdmin)
                        .withStatus(200)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))));

        // when
        ResponseNews actual = newsService.updateNewsAdmin(ID_NEWS_FOR_IT, updateNewsDto, tokenAdmin);

        // then
        assertThat(actual.title()).isEqualTo(updateNewsDto.title());
        assertThat(actual.text()).isEqualTo(updateNewsDto.text());
    }

    @Test
    void shouldReturnUpdatedNewsFromJournalist() {
        // given
        CreateNewsDtoJournalist createNewsDto = NewsTestData.getCreateNewsDtoJournalist();

        when(authentication.getPrincipal())
                .thenReturn(JwtUserTestData.getJwtUserValidationNews());
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // when
        ResponseNews actual = newsService.updateNewsJournalist(ID_NEWS_FOR_IT_JOURNALIST, createNewsDto);

        // then
        assertThat(actual.title()).isEqualTo(createNewsDto.title());
        assertThat(actual.text()).isEqualTo(createNewsDto.text());
    }

    @Test
    void shouldDeleteNewsById() {
        // given
        when(authentication.getPrincipal())
                .thenReturn(JwtUserTestData.getJwtUserAdmin());
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // when, then
        assertThatNoException().isThrownBy(() -> newsService.deleteNews(ID_NEWS_FOR_IT_DELETE));
    }
}