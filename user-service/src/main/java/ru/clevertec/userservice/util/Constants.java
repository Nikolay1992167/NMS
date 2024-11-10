package ru.clevertec.userservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String ROLE_JOURNALIST = "JOURNALIST";
    public static final String ROLE_SUBSCRIBER = "SUBSCRIBER";

    public static final String EMAIL_ERROR = "Incorrect email format!";
    public static final String SIZE_PASSWORD_ERROR = "The length of the data should be from 3 to 100 characters!";
    public static final String SIZE_NAME_ERROR = "The length of the data should be from 2 to 50 characters!";
}
