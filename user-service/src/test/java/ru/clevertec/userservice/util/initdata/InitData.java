package ru.clevertec.userservice.util.initdata;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class InitData {

    public static String FIRST_NAME_JOURNALIST = "Vlad";
    public static String FIRST_NAME_SUBSCRIBER = "Olga";
    public static String FIRST_NAME_ADMIN = "John";
    public static String FIRST_NAME_INCORRECT = "P";
    public static String LAST_NAME_JOURNALIST = "Suzko";
    public static String LAST_NAME_SUBSCRIBER = "Pronina";
    public static String LAST_NAME_ADMIN = "Kenedy";
    public static String LAST_NAME_INCORRECT = "S";

    public static String EMAIL_ADMIN = "dronov@google.com";
    public static String EMAIL_JOURNALIST = "suzko@mail.ru";
    public static String EMAIL_JOURNALIST_FOR_IT = "smirnov@google.com";
    public static String EMAIL_ADMIN_FOR_IT = "dronov@google.com";
    public static String EMAIL_SUBSCRIBER = "pronina@google.com";
    public static String EMAIL_INCORRECT = "incorrect";
    public static String EMAIL_NOT_EXIST = "nikolay@mail.by";

    public static String PASSWORD_JOURNALIST = "147896";
    public static String PASSWORD_JOURNALIST_FOR_IT = "654321";
    public static String PASSWORD_SUBSCRIBER = "654321";
    public static String PASSWORD_INCORRECT = "6";

    public static UUID ID_ADMIN = UUID.fromString("44212253-a305-4495-9982-45e833aa74ac");
    public static UUID ID_JOURNALIST = UUID.fromString("f2361e91-718e-41ad-9ddc-4be05ebc09b5");
    public static UUID ID_JOURNALIST_FOR_IT = UUID.fromString("b3afa636-8006-42fe-961e-21ae926b3265");
    public static UUID ID_ADMIN_FOR_IT = UUID.fromString("44212253-a305-4495-9982-45e833aa74ac");
    public static UUID ID_SUBSCRIBER = UUID.fromString("42f77a57-3cd7-4e5d-a2f8-78f951fc9a41");
    public static UUID ID_SUBSCRIBER_FOR_IT = UUID.fromString("3a472b53-236d-4cd9-a9d3-0d413ad3b903");
    public static UUID ID_NOT_EXIST = UUID.fromString("ddcf4b2b-3ee4-4927-9466-953aa27c8785");

    public static String ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJlbGluQG1haWwucnUiLCJpZCI6ImIwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMiIsInJvbGVzIjpbIkpPVVJOQUxJU1QiXSwiZXhwIjoxNzA4ODgwNDQ2fQ.BjnqDutQchHRwDAZJdXhzHKxXevhcaKgHX36OWbZTRmG_oeelnbvEXyYrtQ89CnDgygDkH5vgUcsHQccVEewYA";
    public static String REFRESH_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJlbGluQG1haWwucnUiLCJpZCI6ImIwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMiIsImV4cCI6MTcxMTQ2ODg0Nn0.dyEnMlSVVqCBb5oQFHXngGmu2l6jgn2o7rKXyB0jypb0DWCyRzcUbNWyCfW7if4OHUILT4AW0sbMpIVvQEUlFA";
    public static String INCORRECT_TOKEN = "1fhgdfhfdghfghfhdf.2fghdfhdfhdfghfghgfhfghfgh.3lkdkg;dlskg;sld;gdfkgl;kdf;lgd;fklg;dslkg;ldkg;";

    public static UUID CREATED_BY_JOURNALIST = null;
    public static UUID UPDATED_BY_JOURNALIST = UUID.fromString("44212253-a305-4495-9982-45e833aa74ac");
    public static UUID CREATED_BY_SUBSCRIBER = null;
    public static UUID UPDATED_BY_SUBSCRIBER = UUID.fromString("44212253-a305-4495-9982-45e833aa74ac");

    public static LocalDateTime CREATED_AT_JOURNALIST = LocalDateTime.of(2024, 1, 2, 12, 0, 0);
    public static LocalDateTime UPDATED_AT_JOURNALIST = LocalDateTime.of(2024, 1, 3, 12, 0, 0);
    public static LocalDateTime CREATED_AT_SUBSCRIBER = LocalDateTime.of(2024, 1, 4, 12, 0, 0);
    public static LocalDateTime UPDATED_AT_SUBSCRIBER = LocalDateTime.of(2024, 1, 5, 12, 0, 0);

    public static UUID ID_ROLE = UUID.fromString("2512c298-6a1d-48d7-a12d-b51069aceb08");
    public static UUID CREATED_BY_ROLE = null;
    public static UUID UPDATED_BY_ROLE = null;
    public static LocalDateTime CREATED_AT_ROLE = LocalDateTime.of(2024, 2, 10, 12, 0, 0);
    public static LocalDateTime UPDATED_AT_ROLE = LocalDateTime.of(2024, 2, 11, 12, 0, 0);
    public static String ROLE_NAME_ADMIN = "ADMIN";

    public static String SECRET_TEST = "73357638792F423F4528482B4D6251655468576D5A7133743677397A24432646";
    public static Long ACCESS_TEST = 1L;
    public static Long REFRESH_TEST = 1L;

    public static String URL_AUTH = "/api/v1/auth";
    public static String URL_USERS = "/api/v1/users";
    public static String URL_ADMIN = "/api/v1/admin";

    public static String BEARER = "Bearer ";
    public static PageRequest DEFAULT_PAGE_REQUEST_FOR_IT = PageRequest.of(0, 15);
}