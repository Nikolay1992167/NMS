package ru.clevertec.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RestClientConfigTest {

    @InjectMocks
    private RestClientConfig restClientConfig;

    @Test
    void shouldReturnExpectedInstanceOfWebclient() {
        // given
        Class<RestClient> expectedClass = RestClient.class;

        // when
        RestClient actualClass = restClientConfig.webClient();

        // then
        assertThat(actualClass).isInstanceOf(expectedClass);
    }
}