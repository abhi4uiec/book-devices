package com.phone.devices.config;

import com.phone.devices.constants.Constants;
import com.phone.devices.domain.user.UserRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("it")
class E2ESecurityConfigTest {

    private final static String PASSWORD = "Password123";

    private final static String ROLE_USER = "ROLE_USER";

    private final static String ROLE_ADMIN = "ROLE_ADMIN";

    private final static String USER_URI = "/user";

    private final static String BASE_URI = "http://localhost";

    private final static String DEVICE_NAME = "2x Samsung Galaxy S8";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    void whenCreatingUsers_thenNoSecurity() {

        UserRequest userRequest = getUserRequestDTO("user1@example.com", ROLE_USER);

        // Create a user and validate
        given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(userRequest)
                .when()
                .post(USER_URI)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void whenGettingPhoneDetails_withoutUser_then_unauthorized() {

        given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/phones/model")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void whenGettingPhoneDetails_withUser_then_ok() {

        UserRequest userRequest = getUserRequestDTO("user2@example.com", ROLE_USER);

        // Create a user and validate
        given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(userRequest)
                .when()
                .post(USER_URI)
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // Fetch phone details and validate
        given().auth().basic("user2@example.com", PASSWORD)
                .pathParam("model", DEVICE_NAME)
                .get("/phones/{model}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("model", equalTo(DEVICE_NAME))
                .body("bookedDate", equalTo("None"))
                .body("bookedBy", equalTo("None"))
                .body("available", equalTo(true))
                .body("phoneInfo", hasKey("technology"))
                .body("phoneInfo.technology", equalTo("GSM / HSPA / LTE"))
                .body("phoneInfo", hasKey("_2gBands"))
                .body("phoneInfo._2gBands", equalTo("GSM 850 / 900 / 1800 / 1900 - SIM 1 & SIM 2 (dual-SIM model only)"))
                .body("phoneInfo", hasKey("_3gBands"))
                .body("phoneInfo._3gBands", equalTo("HSDPA 850 / 900 / 1700(AWS) / 1900 / 2100"))
                .body("phoneInfo", hasKey("_4gBands"))
                .body("phoneInfo._4gBands", equalTo("1, 2, 3, 4, 5, 7, 8, 12, 13, 17, 18, 19, 20, 25, 26, 28, 32, 66, 38, 39, 40, 41"));
    }

    @Test
    void whenBookingPhone_withUserWithoutAdminPrivilege_then_AccessDenied() {

        UserRequest userRequest = getUserRequestDTO("user3@example.com", ROLE_USER);

        // Create a user and validate
        given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(userRequest)
                .when()
                .post(USER_URI)
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // Access denied when booking phone
        given().auth().basic("user3@example.com", PASSWORD)
                .pathParam("model", DEVICE_NAME)
                .queryParam("bookedBy", "Abhishek")
                .post("/phones/{model}/book")
                .then()
                .assertThat()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void whenBookingPhone_withUserWithAdminPrivilege_then_ok() {

        UserRequest userRequest = getUserRequestDTO("admin1@example.com", ROLE_ADMIN);

        // Create a user and validate
        given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(userRequest)
                .when()
                .post(USER_URI)
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // Phone successfully booked when using admin role
        given().auth().basic("admin1@example.com", PASSWORD)
                .pathParam("model", DEVICE_NAME)
                .queryParam("bookedBy", "Abhishek")
                .post("/phones/{model}/book")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("model", equalTo(DEVICE_NAME))
                .body("bookedDate", notNullValue())
                .body("bookedBy", equalTo("Abhishek"))
                .body("available", equalTo(false))
                .body("phoneInfo", hasKey("technology"))
                .body("phoneInfo.technology", equalTo("GSM / HSPA / LTE"))
                .body("phoneInfo", hasKey("_2gBands"))
                .body("phoneInfo._2gBands", equalTo("GSM 850 / 900 / 1800 / 1900 - SIM 1 & SIM 2 (dual-SIM model only)"))
                .body("phoneInfo", hasKey("_3gBands"))
                .body("phoneInfo._3gBands", equalTo("HSDPA 850 / 900 / 1700(AWS) / 1900 / 2100"))
                .body("phoneInfo", hasKey("_4gBands"))
                .body("phoneInfo._4gBands", equalTo("1, 2, 3, 4, 5, 7, 8, 12, 13, 17, 18, 19, 20, 25, 26, 28, 32, 66, 38, 39, 40, 41"));
    }

    @Test
    void whenReturningPhone_phoneNotBooked_then_PhoneNotReturnedException() {

        UserRequest userRequest = getUserRequestDTO("user4@example.com", ROLE_USER);

        // Create a user and validate
        given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(userRequest)
                .when()
                .post(USER_URI)
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // Can't return the phone as it was not booked
        given().auth().basic("user4@example.com", PASSWORD)
                .pathParam("model", DEVICE_NAME)
                .put("/phones/{model}/return")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void whenReturningPhone_phoneBooked_then_phoneReturnedSuccessful() {

        UserRequest userRequest = getUserRequestDTO("admin2@example.com", ROLE_ADMIN);

        // Create a user and validate
        given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(userRequest)
                .when()
                .post(USER_URI)
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // Book a phone
        given().auth().basic("admin2@example.com", PASSWORD)
                .pathParam("model", DEVICE_NAME)
                .queryParam("bookedBy", "Abhishek")
                .post("/phones/{model}/book")
                .then()
                .statusCode(HttpStatus.OK.value());

        // Successfully return a phone
        given().auth().basic("admin2@example.com", PASSWORD)
                .pathParam("model", DEVICE_NAME)
                .put("/phones/{model}/return")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("model", equalTo(DEVICE_NAME))
                .body("bookedDate", equalTo(Constants.NONE))
                .body("bookedBy", equalTo(Constants.NONE))
                .body("available", equalTo(true))
                .body("phoneInfo", hasKey("technology"))
                .body("phoneInfo.technology", equalTo("GSM / HSPA / LTE"))
                .body("phoneInfo", hasKey("_2gBands"))
                .body("phoneInfo._2gBands", equalTo("GSM 850 / 900 / 1800 / 1900 - SIM 1 & SIM 2 (dual-SIM model only)"))
                .body("phoneInfo", hasKey("_3gBands"))
                .body("phoneInfo._3gBands", equalTo("HSDPA 850 / 900 / 1700(AWS) / 1900 / 2100"))
                .body("phoneInfo", hasKey("_4gBands"))
                .body("phoneInfo._4gBands", equalTo("1, 2, 3, 4, 5, 7, 8, 12, 13, 17, 18, 19, 20, 25, 26, 28, 32, 66, 38, 39, 40, 41"));
    }

    private UserRequest getUserRequestDTO(final String userName, final String roleName) {
        return new UserRequest(userName, PASSWORD, roleName);
    }
}

