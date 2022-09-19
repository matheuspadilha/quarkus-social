package io.github.matheuspadilha.quarkussocial.resource;

import io.github.matheuspadilha.quarkussocial.domain.model.User;
import io.github.matheuspadilha.quarkussocial.domain.repository.UserRepository;
import io.github.matheuspadilha.quarkussocial.resource.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRepository userRepository;

    Long userId;

    @BeforeEach
    @Transactional
    void setUp() {
        User user = new User();
        user.setAge(27);
        user.setName("John Doe");

        userRepository.persist(user);
        userId = user.getId();
    }

    @Test
    @DisplayName("Should create a post for a user")
    void createPostTest() {
        CreatePostRequest post = CreatePostRequest.builder()
                .text("Lorem ipsum")
                .build();

        given()
            .contentType(ContentType.JSON)
            .body(post)
            .pathParam("userId", userId)
        .when()
            .post()
        .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("Should return 404 when trying to make a post for an inexistent user")
    void postForAninexistentUserTest() {
        CreatePostRequest post = CreatePostRequest.builder()
                .text("Lorem ipsum")
                .build();

        Long inexistentUserId = 999L;

        given()
            .contentType(ContentType.JSON)
            .body(post)
            .pathParam("userId", inexistentUserId)
        .when()
            .post()
        .then()
            .statusCode(404);
    }

}