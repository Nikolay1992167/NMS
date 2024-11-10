package ru.clevertec.service.impl;

import ru.clevertec.dto.request.CreateNewsDto;
import ru.clevertec.dto.request.CreateNewsDtoJournalist;
import ru.clevertec.dto.request.Filter;
import ru.clevertec.dto.response.ResponseNews;
import ru.clevertec.enams.ErrorMessage;
import ru.clevertec.entity.News;
import ru.clevertec.exception.handling.exception.AccessException;
import ru.clevertec.exception.handling.exception.CreateObjectException;
import ru.clevertec.exception.handling.exception.NotFoundException;
import ru.clevertec.mapper.NewsMapper;
import ru.clevertec.repository.NewsRepository;
import ru.clevertec.service.NewsService;
import ru.clevertec.service.UserDataService;
import ru.clevertec.util.AuthUtil;
import ru.clevertec.validation.NewsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {
    private final NewsValidator newsValidator;

    private final NewsMapper newsMapper;

    private final NewsRepository newsRepository;

    private final UserDataService userDataService;


    @Override
    @Cacheable(value = "ResponseNews", key = "#newsId")
    public ResponseNews findNewsById(UUID newsId) {
        return newsRepository.findById(newsId)
                .map(newsMapper::toResponseNews)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NEWS_NOT_FOUND.getMessage() + newsId));
    }

    @Override
    public Page<ResponseNews> getAllNews(Pageable pageable) {
        return newsRepository.findAll(pageable)
                .map(newsMapper::toResponseNews);
    }

    @Override
    public Page<ResponseNews> findNewsByFilter(Filter filter, Pageable pageable) {
        return Optional.ofNullable(filter.part())
                .map(part -> "%" + part + "%")
                .map(part -> Specification.anyOf(
                        (Specification<News>) (root, query, cb) -> cb.like(root.get("text"), part),
                        (Specification<News>) (root, query, cb) -> cb.like(root.get("title"), part)))
                .map(spec -> newsRepository.findAll(spec, pageable))
                .orElseGet(() -> newsRepository.findAll(pageable))
                .map(newsMapper::toResponseNews);
    }

    @Override
    @Transactional
    @CachePut(value = "ResponseNews", key = "#result.id()")
    public ResponseNews createNewsAdmin(CreateNewsDto createNewsDto, String authorizationToken) {
        userDataService.getUserData(createNewsDto.idAuthor(), authorizationToken);
        return Optional.of(createNewsDto)
                .map(newsMapper::toNews)
                .map(news -> {
                    news.setCreatedBy(AuthUtil.getId());
                    return newsRepository.saveAndFlush(news);
                })
                .map(newsMapper::toResponseNews)
                .orElseThrow(() -> new CreateObjectException(ErrorMessage.ERROR_CREATE_OBJECT.getMessage()));
    }

    @Override
    @Transactional
    @CachePut(value = "ResponseNews", key = "#result.id()")
    public ResponseNews createNewsJournalist(CreateNewsDtoJournalist createNewsDto) {
        UUID userId = AuthUtil.getId();
        return Optional.of(createNewsDto)
                .map(newsMapper::toNews)
                .map(news -> {
                    news.setCreatedBy(userId);
                    news.setIdAuthor(userId);
                    return newsRepository.saveAndFlush(news);
                })
                .map(newsMapper::toResponseNews)
                .orElseThrow(() -> new CreateObjectException(ErrorMessage.ERROR_CREATE_OBJECT.getMessage()));
    }

    @Override
    @Transactional
    @CachePut(value = "ResponseNews", key = "#result.id()")
    public ResponseNews updateNewsAdmin(UUID newsId, CreateNewsDto newsDto, String authorizationToken) {
        userDataService.getUserData(newsDto.idAuthor(), authorizationToken);
        return newsRepository.findById(newsId)
                .map(current -> {
                    News updateNews = newsMapper.merge(current, newsDto);
                    updateNews.setUpdatedBy(AuthUtil.getId());
                    return newsRepository.saveAndFlush(updateNews);
                })
                .map(newsMapper::toResponseNews)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NEWS_NOT_FOUND.getMessage() + newsId));
    }

    @Override
    @Transactional
    @CachePut(value = "ResponseNews", key = "#result.id()")
    public ResponseNews updateNewsJournalist(UUID newsId, CreateNewsDtoJournalist newsDto) {
        UUID userId = AuthUtil.getId();
        if (newsValidator.isAuthor(newsId, userId)) {
            return newsRepository.findById(newsId)
                    .map(current -> {
                        News updateNews = newsMapper.merge(current, newsDto);
                        updateNews.setUpdatedBy(userId);
                        return newsRepository.saveAndFlush(updateNews);
                    })
                    .map(newsMapper::toResponseNews)
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.NEWS_NOT_FOUND.getMessage() + newsId));
        }
        return null;
    }

    @Override
    @Transactional
    @CacheEvict(value = "ResponseNews", key = "#newsId")
    public void deleteNews(UUID newsId) {
        if (!newsValidator.isOwnerRightChange(newsId)) {
            throw new AccessException(ErrorMessage.ERROR_CHANGE.getMessage());
        }
        newsRepository.deleteById(newsId);
    }
}