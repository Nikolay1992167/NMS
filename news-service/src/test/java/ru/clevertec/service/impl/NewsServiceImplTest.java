package ru.clevertec.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.dto.request.CreateNewsDto;
import ru.clevertec.dto.request.CreateNewsDtoJournalist;
import ru.clevertec.dto.request.Filter;
import ru.clevertec.dto.request.JwtUser;
import ru.clevertec.dto.response.ResponseNews;
import ru.clevertec.dto.response.UserResponse;
import ru.clevertec.enams.ErrorMessage;
import ru.clevertec.entity.News;
import ru.clevertec.exception.handling.exception.AccessException;
import ru.clevertec.exception.handling.exception.CreateObjectException;
import ru.clevertec.exception.handling.exception.NotFoundException;
import ru.clevertec.mapper.NewsMapper;
import ru.clevertec.repository.NewsRepository;
import ru.clevertec.service.UserDataService;
import ru.clevertec.util.testdata.FilterTestData;
import ru.clevertec.util.testdata.JwtUserTestData;
import ru.clevertec.util.testdata.NewsTestData;
import ru.clevertec.util.testdata.UserTestData;
import ru.clevertec.validation.NewsValidator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.util.init.InitData.AUTHORIZATION_TOKEN;
import static ru.clevertec.util.init.InitData.DEFAULT_PAGE_REQUEST_FOR_IT;
import static ru.clevertec.util.init.InitData.ID_JOURNALIST;
import static ru.clevertec.util.init.InitData.ID_NEWS;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @InjectMocks
    private NewsServiceImpl newsService;

    @Mock
    private NewsValidator newsValidator;

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private UserDataService userDataService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Nested
    class FindNewsById {

        @Test
        void shouldReturnExpectedValue() {
            // given
            UUID newsId = ID_NEWS;
            News news = NewsTestData.getNews();
            ResponseNews expected = NewsTestData.getResponseNews();
            when(newsRepository.findById(newsId))
                    .thenReturn(Optional.of(news));
            when(newsMapper.toResponseNews(news))
                    .thenReturn(expected);
            // when
            ResponseNews actual = newsService.findNewsById(newsId);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldThrowExceptionWhenNewsIsNotExist() {
            // given
            UUID newsId = UUID.randomUUID();

            // when
            assertThatThrownBy(() -> newsService.findNewsById(newsId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ErrorMessage.NEWS_NOT_FOUND.getMessage() + newsId);
        }
    }

    @Nested
    class GetAllNews {
        @Test
        void shouldReturnPageOfResponseNews() {
            // given
            int expectedSize = 1;
            List<News> newsList = List.of(NewsTestData.getNews());
            Page<News> page = new PageImpl<>(newsList);
            when(newsRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<ResponseNews> actual = newsService.getAllNews(DEFAULT_PAGE_REQUEST_FOR_IT);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }

        @Test
        void shouldCheckEmpty() {
            // given
            Page<News> page = new PageImpl<>(List.of());
            when(newsRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<ResponseNews> actual = newsService.getAllNews(DEFAULT_PAGE_REQUEST_FOR_IT);

            // then
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class FindNewsByFilter {

        @Test
        void shouldReturnPageOfResponseNews() {
            // given
            Pageable pageable = Pageable.unpaged();
            Filter filter = FilterTestData.getFilter();
            List<News> newsList = List.of(NewsTestData.getNews());
            PageImpl<News> newsPage = new PageImpl<>(newsList, pageable, 1);
            List<ResponseNews> expectedNews = List.of(NewsTestData.getResponseNews());

            doReturn(newsPage)
                    .when(newsRepository).findAll(any(Specification.class), eq(pageable));
            IntStream.range(0, newsList.size())
                    .forEach(i -> doReturn(expectedNews.get(i))
                            .when(newsMapper).toResponseNews(newsList.get(i)));

            // when
            List<ResponseNews> actualNews = newsService
                    .findNewsByFilter(filter, pageable)
                    .getContent();

            // then
            assertThat(actualNews).isEqualTo(expectedNews);
        }

        @Test
        void checkEmpty() {
            // given
            Pageable pageable = Pageable.unpaged();
            Filter emptyFilter = new Filter(null);
            List<News> newsList = List.of(NewsTestData.getNews());
            PageImpl<News> newsPage = new PageImpl<>(newsList, pageable, 1);
            List<ResponseNews> expectedNews = List.of(NewsTestData.getResponseNews());

            doReturn(newsPage)
                    .when(newsRepository).findAll(pageable);
            IntStream.range(0, newsList.size())
                    .forEach(i -> doReturn(expectedNews.get(i))
                            .when(newsMapper).toResponseNews(newsList.get(i)));

            // when
            List<ResponseNews> actualNews = newsService
                    .findNewsByFilter(emptyFilter, pageable)
                    .getContent();

            // then
            assertThat(actualNews).isEqualTo(expectedNews);
        }
    }

    @Nested
    class CreateNewsAdmin {

        @Test
        void shouldReturnExpectedValue() {
            // given
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            UserResponse userResponse = UserTestData.getUserResponse();
            News news = NewsTestData.getNews();
            CreateNewsDto createNewsDto = NewsTestData.getCreateNewsDto();
            ResponseNews expected = NewsTestData.getResponseNews();
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(userDataService.getUserData(createNewsDto.idAuthor(), AUTHORIZATION_TOKEN))
                    .thenReturn(userResponse);
            when(newsMapper.toNews(createNewsDto))
                    .thenReturn(news);
            when(newsRepository.saveAndFlush(any(News.class)))
                    .thenReturn(news);
            when(newsMapper.toResponseNews(news))
                    .thenReturn(expected);

            // when
            ResponseNews actual = newsService.createNewsAdmin(createNewsDto, AUTHORIZATION_TOKEN);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldThrowException() {
            // given
            UserResponse userResponse = UserTestData.getUserResponse();
            CreateNewsDto createNewsDto = NewsTestData.getCreateNewsDto();
            when(userDataService.getUserData(createNewsDto.idAuthor(), AUTHORIZATION_TOKEN))
                    .thenReturn(userResponse);
            when(newsMapper.toNews(createNewsDto))
                    .thenReturn(null);

            // when, then
            assertThatThrownBy(() -> newsService.createNewsAdmin(createNewsDto, AUTHORIZATION_TOKEN))
                    .isInstanceOf(CreateObjectException.class)
                    .hasMessageContaining(ErrorMessage.ERROR_CREATE_OBJECT.getMessage());
        }
    }

    @Nested
    class CreateNewsJournalist {

        @Test
        void shouldReturnExpectedValue() {
            // given
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            News news = NewsTestData.getNews();
            CreateNewsDtoJournalist createNewsDtoJournalist = NewsTestData.getCreateNewsDtoJournalist();
            ResponseNews expected = NewsTestData.getResponseNews();
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(newsMapper.toNews(createNewsDtoJournalist))
                    .thenReturn(news);
            when(newsRepository.saveAndFlush(any(News.class)))
                    .thenReturn(news);
            when(newsMapper.toResponseNews(news))
                    .thenReturn(expected);

            // when
            ResponseNews actual = newsService.createNewsJournalist(createNewsDtoJournalist);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldThrowException() {
            // given
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            CreateNewsDtoJournalist createNewsDtoJournalist = NewsTestData.getCreateNewsDtoJournalist();
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(newsMapper.toNews(createNewsDtoJournalist))
                    .thenReturn(null);

            // when, then
            assertThatThrownBy(() -> newsService.createNewsJournalist(createNewsDtoJournalist))
                    .isInstanceOf(CreateObjectException.class)
                    .hasMessageContaining(ErrorMessage.ERROR_CREATE_OBJECT.getMessage());
        }
    }

    @Nested
    class UpdateNewsAdmin {

        @Test
        void shouldReturnExpectedValue() {
            // given
            UUID newsId = ID_NEWS;
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            UserResponse userResponse = UserTestData.getUserResponse();
            News news = NewsTestData.getNews();
            CreateNewsDto createNewsDto = NewsTestData.getCreateNewsDto();
            ResponseNews expected = NewsTestData.getResponseNews();
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(userDataService.getUserData(createNewsDto.idAuthor(), AUTHORIZATION_TOKEN))
                    .thenReturn(userResponse);
            when(newsRepository.findById(newsId))
                    .thenReturn(Optional.of(news));
            when(newsMapper.merge(news, createNewsDto))
                    .thenReturn(news);
            when(newsRepository.saveAndFlush(any(News.class)))
                    .thenReturn(news);
            when(newsMapper.toResponseNews(news))
                    .thenReturn(expected);

            // when
            ResponseNews actual = newsService.updateNewsAdmin(newsId, createNewsDto, AUTHORIZATION_TOKEN);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldThrowException() {
            // given
            UUID newsId = ID_NEWS;
            UserResponse userResponse = UserTestData.getUserResponse();
            CreateNewsDto createNewsDto = NewsTestData.getCreateNewsDto();
            when(userDataService.getUserData(createNewsDto.idAuthor(), AUTHORIZATION_TOKEN))
                    .thenReturn(userResponse);
            when(newsRepository.findById(newsId))
                    .thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> newsService.updateNewsAdmin(newsId, createNewsDto, AUTHORIZATION_TOKEN))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ErrorMessage.NEWS_NOT_FOUND.getMessage() + newsId);
        }
    }

    @Nested
    class UpdateNewsJournalist {

        @Test
        void shouldReturnNull() {
            // given
            UUID newsId = ID_NEWS;
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            CreateNewsDtoJournalist createNewsDto = NewsTestData.getCreateNewsDtoJournalist();
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(newsValidator.isAuthor(newsId, ID_JOURNALIST))
                    .thenReturn(Boolean.FALSE);

            // when
            ResponseNews actual = newsService.updateNewsJournalist(newsId, createNewsDto);

            // then
            assertThat(actual).isNull();
        }

        @Test
        void shouldReturnExpectedValue() {
            // given
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            News news = NewsTestData.getNews();
            CreateNewsDtoJournalist createNewsDto = NewsTestData.getCreateNewsDtoJournalist();
            ResponseNews expected = NewsTestData.getResponseNews();
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(newsValidator.isAuthor(ID_NEWS, ID_JOURNALIST))
                    .thenReturn(Boolean.TRUE);
            when(newsRepository.findById(ID_NEWS))
                    .thenReturn(Optional.of(news));
            when(newsMapper.merge(news, createNewsDto))
                    .thenReturn(news);
            when(newsRepository.saveAndFlush(news))
                    .thenReturn(news);
            when(newsMapper.toResponseNews(news))
                    .thenReturn(expected);

            // when
            ResponseNews actual = newsService.updateNewsJournalist(ID_NEWS, createNewsDto);

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class DeleteNews {

        @Test
        void shouldDeleteNewsWhenUserIsOwner() {
            // given
            UUID newsId = ID_NEWS;
            when(newsValidator.isOwnerRightChange(newsId))
                    .thenReturn(Boolean.TRUE);
            doNothing().when(newsRepository).deleteById(newsId);

            // when
            newsService.deleteNews(newsId);

            // then
            verify(newsValidator, times(1)).isOwnerRightChange(ID_NEWS);
        }

        @Test
        void shouldThrowAccessExceptionWhenUserIsNotOwner() {
            // given
            UUID newsId = ID_NEWS;
            when(newsValidator.isOwnerRightChange(newsId))
                    .thenReturn(Boolean.FALSE);

            // when & then
            assertThatThrownBy(() -> newsService.deleteNews(newsId))
                    .isInstanceOf(AccessException.class)
                    .hasMessage(ErrorMessage.ERROR_CHANGE.getMessage());
        }
    }
}