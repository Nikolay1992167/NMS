package ru.clevertec.util.testdata;

import ru.clevertec.dto.request.CreateNewsDto;
import ru.clevertec.dto.request.CreateNewsDtoJournalist;
import ru.clevertec.dto.response.ResponseNews;
import ru.clevertec.entity.News;

import java.util.List;

import static ru.clevertec.util.init.InitData.CREATED_AT;
import static ru.clevertec.util.init.InitData.CREATED_BY;
import static ru.clevertec.util.init.InitData.ID_ADMIN_FOR_IT;
import static ru.clevertec.util.init.InitData.ID_AUTHOR_NEWS;
import static ru.clevertec.util.init.InitData.ID_NEWS;
import static ru.clevertec.util.init.InitData.TEXT_NEWS;
import static ru.clevertec.util.init.InitData.TITLE_NEWS;
import static ru.clevertec.util.init.InitData.UPDATED_AT;
import static ru.clevertec.util.init.InitData.UPDATED_BY;

public class NewsTestData {

    public static CreateNewsDtoJournalist getCreateNewsDtoJournalistIncorrect() {
        return CreateNewsDtoJournalist.builder()
                .title("")
                .text(TEXT_NEWS)
                .build();
    }

    public static CreateNewsDtoJournalist getCreateNewsDtoJournalist() {
        return CreateNewsDtoJournalist.builder()
                .title(TITLE_NEWS)
                .text(TEXT_NEWS)
                .build();
    }

    public static CreateNewsDto getCreateNewsDtoITForAdmin() {
        return CreateNewsDto.builder()
                .title(TITLE_NEWS)
                .text(TEXT_NEWS)
                .idAuthor(ID_ADMIN_FOR_IT)
                .build();
    }

    public static CreateNewsDto getCreateNewsDtoIncorrect() {
        return CreateNewsDto.builder()
                .title("")
                .text("")
                .idAuthor(ID_AUTHOR_NEWS)
                .build();
    }

    public static CreateNewsDto getCreateNewsDto() {
        return CreateNewsDto.builder()
                .title(TITLE_NEWS)
                .text(TEXT_NEWS)
                .idAuthor(ID_AUTHOR_NEWS)
                .build();
    }

    public static News getNews() {
        return News.builder()
                .id(ID_NEWS)
                .createdBy(CREATED_BY)
                .updatedBy(UPDATED_BY)
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .title(TITLE_NEWS)
                .text(TEXT_NEWS)
                .idAuthor(ID_AUTHOR_NEWS)
                .comments(List.of())
                .build();
    }

    public static ResponseNews getResponseNews() {
        return ResponseNews.builder()
                .id(ID_NEWS)
                .createdBy(CREATED_BY)
                .updatedBy(UPDATED_BY)
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .title(TITLE_NEWS)
                .text(TEXT_NEWS)
                .idAuthor(ID_AUTHOR_NEWS)
                .build();
    }
}