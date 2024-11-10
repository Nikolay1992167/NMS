package ru.clevertec.util.init;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class InitData {
    public static UUID ID_ROLE = UUID.fromString("2512c298-6a1d-48d7-a12d-b51069aceb08");
    public static String ROLE_NAME_JOURNALIST = "JOURNALIST";
    public static String ROLE_NAME_ADMIN = "ADMIN";
    public static String ROLE_NAME_SUBSCRIBER = "SUBSCRIBER";
    public static UUID ID_USER = UUID.fromString("b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12");
    public static String USER_FIRST_NAME = "Egor";
    public static String USER_LAST_NAME = "Strelin";
    public static String USER_PASSWORD = "$2a$10$uuwsQHbWZMIUMTuxKwij8e/l5zea9.Q2XW0eG3Bs/2fUMarbqiymG";
    public static String USER_EMAIL = "strelin@mail.ru";
    public static UUID ID_NEWS = UUID.fromString("7bb84799-46d7-4afe-80fa-90748469865a");
    public static UUID CREATED_BY = UUID.fromString("9439e220-7a43-4193-90d3-325ed3e5c8bd");
    public static UUID UPDATED_BY = UUID.fromString("e15ff677-b3b1-4d9f-8972-810805a2afa6");
    public static LocalDateTime CREATED_AT = LocalDateTime.now();
    public static LocalDateTime UPDATED_AT = LocalDateTime.now();
    public static String TITLE_NEWS = "New Features in Java 21";
    public static String TEXT_NEWS = "In Java 21 added Record Patterns (JEP 440), Pattern Matching for switch (JEP 441), String Literal, Virtual Threads, Sequenced Collections.";
    public static UUID ID_AUTHOR_NEWS = UUID.fromString("b3afa636-8006-42fe-961e-21ae926b3265");

    public static PageRequest DEFAULT_PAGE_REQUEST_FOR_IT = PageRequest.of(0, 15);
    public static UUID ID_JOURNALIST = UUID.fromString("f2361e91-718e-41ad-9ddc-4be05ebc09b5");
    public static String FIRST_NAME_JOURNALIST = "Vlad";
    public static String LAST_NAME_JOURNALIST = "Suzko";
    public static String EMAIL_JOURNALIST = "suzko@mail.ru";
    public static String PASSWORD_JOURNALIST = "147896";
    public static String AUTHORIZATION_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJlbGluQG1haWwucnUiLCJpZCI6ImIwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMiIsInJvbGVzIjpbIkpPVVJOQUxJU1QiXSwiZXhwIjoxNzA4ODgwNDQ2fQ.BjnqDutQchHRwDAZJdXhzHKxXevhcaKgHX36OWbZTRmG_oeelnbvEXyYrtQ89CnDgygDkH5vgUcsHQccVEewYA";
    public static UUID ID_COMMENT = UUID.fromString("54bb52ec-685f-4c57-a538-56355797a037");
    public static String TEXT_COMMENT = "All the best for Java developers!";
    public static String TEXT_UPDATE_COMMENT = "Updated 21 Java!";
    public static String USER_NAME_COMMENT = "Alex Duk";

    public static String BEARER = "Bearer ";
    public static String URL_COMMENT = "/api/v1/comment";
    public static String URL_NEWS = "/api/v1/news";
    public static String URL_DATA_BY_TOKEN = "/api/v1/users/details";
    public static String URL_DATA_BY_ID_USER_AND_TOKEN = "/api/v1/admin/";
    public static UUID ID_COMMENT_FOR_IT = UUID.fromString("98484a32-71c6-44cf-8686-ca8f528d6ad5");
    public static UUID ID_COMMENT_FOR_IT_DELETE = UUID.fromString("8cc4b223-d135-4a4e-b0c3-dc914036c539");
    public static UUID ID_NEWS_FOR_IT = UUID.fromString("acd9356c-07b9-422c-9140-95fd8d111bf6");
    public static UUID ID_NEWS_FOR_IT_DELETE = UUID.fromString("166f30de-5356-4385-8d0d-eb1d37b07689");
    public static UUID ID_NEWS_FOR_IT_JOURNALIST = UUID.fromString("e551e6c4-e1df-4084-8872-51c76e0b4801");
    public static UUID ID_NOT_EXIST = UUID.fromString("98484a32-71c6-44cf-8686-ca8f528d6ad9");
    public static UUID ID_ADMIN_FOR_IT = UUID.fromString("44212253-a305-4495-9982-45e833aa74ac");
    public static UUID ID_JOURNALIST_FOR_IT = UUID.fromString("b3afa636-8006-42fe-961e-21ae926b3265");
    public static UUID ID_SUBSCRIBER_FOR_IT = UUID.fromString("3a472b53-236d-4cd9-a9d3-0d413ad3b903");
    public static String EMAIL_ADMIN_FOR_IT = "dronov@google.com";
    public static String EMAIL_JOURNALIST_FOR_IT = "smirnov@google.com";
    public static String EMAIL_SUBSCRIBER_FOR_IT = "pronina@google.com";
}