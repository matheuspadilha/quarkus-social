package io.github.matheuspadilha.quarkussocial.resource;

import io.github.matheuspadilha.quarkussocial.resource.dto.CreateUserRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class UserResourceTest {

    @Test
    @DisplayName("Should create an user successfully")
    void createUserTest() {
        CreateUserRequest user = CreateUserRequest.builder()
                .name("John Doe")
                .age(27)
                .build();

        Response response =
            given()
                .contentType(ContentType.JSON)
                .body(user)
            .when()
                .post("/users")
            .then()
                .extract().response();

        assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("Should return error when json is not valid")
    void createUserValidationErrorTest() {
        CreateUserRequest user = CreateUserRequest.builder()
                .name(null)
                .age(null)
                .build();

        Response response =
                given()
                    .contentType(ContentType.JSON)
                    .body(user)
                .when()
                    .post("/users")
                .then()
                    .extract().response();

        assertEquals(400, response.statusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));
    }
}