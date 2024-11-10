package ru.clevertec.mapper;

import ru.clevertec.dto.request.CreateNewsDto;
import ru.clevertec.dto.request.CreateNewsDtoJournalist;
import ru.clevertec.dto.response.ResponseNews;
import ru.clevertec.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper {

    /**
     * Converts {@link News} to {@link ResponseNews} containing information about the news.
     *
     * @param news object {@link News}.
     * @return {@link ResponseNews} with information about the news.
     */
    ResponseNews toResponseNews(News news);

    /**
     * Converts {@link CreateNewsDto} containing news information to {@link News}.
     *
     * @param createNewsDto object {@link CreateNewsDto}.
     * @return {@link News} without ID, creation time and comments.
     */
    News toNews(CreateNewsDto createNewsDto);

    /**
     * Converts {@link CreateNewsDto} containing news information to {@link News}.
     *
     * @param createNewsDtoJournalist object {@link CreateNewsDtoJournalist}.
     * @return {@link News} without ID, creation time and comments.
     */
    News toNews(CreateNewsDtoJournalist createNewsDtoJournalist);

    /**
     * Updates {@link News} based on information from {@link CreateNewsDto}.
     *
     * @param oldNews       updated {@link News}.
     * @param createNewsDto object {@link CreateNewsDto} with information about the object.
     * @return {@link News} updated news.
     */
    News merge(@MappingTarget News oldNews, CreateNewsDto createNewsDto);

    /**
     * Updates {@link News} based on information from {@link CreateNewsDtoJournalist}.
     *
     * @param oldNews                 updated {@link News}.
     * @param createNewsDtoJournalist object {@link CreateNewsDtoJournalist} with information about the object.
     * @return {@link News} updated news.
     */
    News merge(@MappingTarget News oldNews, CreateNewsDtoJournalist createNewsDtoJournalist);
}