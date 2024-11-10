package ru.clevertec.service;

import ru.clevertec.dto.request.CreateNewsDto;
import ru.clevertec.dto.request.CreateNewsDtoJournalist;
import ru.clevertec.dto.request.Filter;
import ru.clevertec.dto.response.ResponseNews;
import ru.clevertec.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NewsService {

    /**
     * Searches for the saved {@link News} and returns {@link ResponseNews} with information about the news.
     *
     * @param newsId of news.
     * @return {@link ResponseNews} with information about {@link News}.
     */
    ResponseNews findNewsById(UUID newsId);

    /**
     * Method for getting the {@link ResponseNews} page with information about the news
     * with the specified pagination parameters.
     *
     * @param pageable object {@link Pageable} containing information about the page number and size.
     * @return object {@link Page} with {@link ResponseNews} with information about the news.
     */
    Page<ResponseNews> getAllNews(Pageable pageable);

    /**
     * Returns {@link Page} containing {@link ResponseNews} objects
     * with preset pagination and filtering parameters.
     *
     * @param filter the {@link Filter} object containing the filtering criteria.
     * @param pageable object {@link Pageable} containing information about the page number and size.
     * @return object {@link Page} containing objects {@link ResponseNews} with information about the news.
     */
    Page<ResponseNews> findNewsByFilter(Filter filter, Pageable pageable);

    /**
     * Creates a new {@link News} for user with role 'ADMIN' created based on the specified {@link CreateNewsDto} object
     * and returns {@link ResponseNews} with information about the news.
     *
     * @param createNewsDto object {@link CreateNewsDto} containing data for creating news.
     * @param authorizationToken a string containing the authentication authorizationToken in the request header.
     * @return object {@link ResponseNews} with information about {@link News}.
     */
    ResponseNews createNewsAdmin(CreateNewsDto createNewsDto, String authorizationToken);

    /**
     * Creates a new {@link News} for user with role 'JOURNALIST' created based on the specified {@link CreateNewsDto} object
     * and returns {@link ResponseNews} with information about the news.
     *
     * @param createNewsDto object {@link CreateNewsDto} containing data for creating news.
     * @return object {@link ResponseNews} with information about {@link News}.
     */
    ResponseNews createNewsJournalist(CreateNewsDtoJournalist createNewsDto);

    /**
     * Updates an existing {@link News} using data from {@link CreateNewsDto} for user with role 'ADMIN'.
     *
     * @param newsId of news.
     * @param newsDto object {@link CreateNewsDto} containing data for updating the news.
     * @param authorizationToken a string containing the authentication authorizationToken in the request header.
     * @return object {@link ResponseNews} with information about the updated {@link News}.
     */
    ResponseNews updateNewsAdmin(UUID newsId, CreateNewsDto newsDto, String authorizationToken);

    /**
     * Updates an existing {@link News} using data from {@link CreateNewsDtoJournalist} for user with role 'JOURNALIST'.
     *
     * @param newsId  of news.
     * @param newsDto object {@link CreateNewsDtoJournalist} containing data for updating the news.
     * @return object {@link ResponseNews} with information about the updated {@link News}.
     */
    ResponseNews updateNewsJournalist(UUID newsId, CreateNewsDtoJournalist newsDto);

    /**
     * Deletes {@link News} by newsId.
     *
     * @param newsId of news.
     */
    void deleteNews(UUID newsId);
}