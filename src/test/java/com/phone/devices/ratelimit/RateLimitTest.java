package com.phone.devices.ratelimit;

import com.phone.devices.domain.user.UserRequestDTO;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("rate-limit")
class RateLimitTest {

    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    void shouldFailOnExceedingRateLimitSet() {

        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .username("admin@example.com")
                .password("TestPassword123")
                .authority("ROLE_ADMIN")
                .build();

        given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(userRequestDTO)
                .when()
                .post("/user")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // first 2 times will pass
        IntStream.range(0, 2).forEach(i -> given().auth().basic("admin@example.com", "TestPassword123")
                .pathParam("model", "2x Samsung Galaxy S8")
                .get("/phones/{model}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value()));

        // next time it'll fail because of rate limit
        given().auth().basic("admin@example.com", "TestPassword123")
                .pathParam("model", "2x Samsung Galaxy S8")
                .get("/phones/{model}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.TOO_MANY_REQUESTS.value())
                .body("message", Matchers.is("You have exhausted your API Request Quota"));
    }

}