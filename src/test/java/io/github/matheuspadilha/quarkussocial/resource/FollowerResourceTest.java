package io.github.matheuspadilha.quarkussocial.resource;

import io.github.matheuspadilha.quarkussocial.domain.model.Follower;
import io.github.matheuspadilha.quarkussocial.domain.model.User;
import io.github.matheuspadilha.quarkussocial.domain.repository.FollowerRepository;
import io.github.matheuspadilha.quarkussocial.domain.repository.UserRepository;
import io.github.matheuspadilha.quarkussocial.resource.dto.FollowerRequest;
import io.github.matheuspadilha.quarkussocial.resource.dto.FollowerResponse;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FollowerResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;

    Long userId;
    Long followerId;

    @BeforeEach
    @Transactional
    void setUp() {
        // usuario padrao dos testes
        User user = new User();
        user.setAge(27);
        user.setName("John Doe");
        userRepository.persist(user);
        userId = user.getId();

        // o seguidor
        User follower = new User();
        follower.setAge(21);
        follower.setName("Lorem");
        userRepository.persist(follower);
        followerId = follower.getId();

        // registro de seguidor
        Follower followerEntity = new Follower();
        followerEntity.setUser(user);
        followerEntity.setFollower(follower);
        followerRepository.persist(followerEntity);
    }

    @Test
    @DisplayName("Should return 409 when Follower Id is equal to User Id")
    @Order(1)
    void sameUserAsFollowerTest() {
        FollowerRequest followerRequest = new FollowerRequest(userId);

        given()
            .contentType(ContentType.JSON)
            .body(followerRequest)
            .pathParam("userId", userId)
        .when()
            .put()
        .then()
            .statusCode(Response.Status.CONFLICT.getStatusCode())
            .body(Matchers.is("You can't follow yourself"));
    }

    @Test
    @DisplayName("Should return 404 on follow a user when User Id doesn't exist")
    @Order(2)
    void userNotFoundWhenTryingToFollowTest() {
        Long inexistentUserId = 999L;
        FollowerRequest followerRequest = new FollowerRequest(userId);

        given()
            .contentType(ContentType.JSON)
            .body(followerRequest)
            .pathParam("userId", inexistentUserId)
        .when()
            .put()
        .then()
            .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should follow a user")
    @Order(3)
    void followUserTest() {
        FollowerRequest followerRequest = new FollowerRequest(followerId);

        given()
            .contentType(ContentType.JSON)
            .body(followerRequest)
            .pathParam("userId", userId)
        .when()
            .put()
        .then()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @DisplayName("Should return 404 on list user followers and User Id doesn't exist")
    @Order(4)
    void userNotFoundWhenListingFollowersTest() {
        Long inexistentUserId = 999L;

        given()
            .contentType(ContentType.JSON)
            .pathParam("userId", inexistentUserId)
        .when()
            .get()
        .then()
            .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should list a user's followers")
    @Order(5)
    void listFollowersTest() {
        io.restassured.response.Response response =
                given()
                    .contentType(ContentType.JSON)
                    .pathParam("userId", userId)
                .when()
                    .get()
                .then()
                    .extract().response();

        Integer followersCount = response.jsonPath().get("followersCount");
        List<FollowerResponse> followersContent = response.jsonPath().get("content");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertEquals(1, followersCount);
        assertEquals(1, followersContent.size());
    }

    @Test
    @DisplayName("Should return 404 on unfollow user and User Id doesn't exist")
    void userNotFoundWhenUnfolloweingAUserTest() {
        Long inexistentUserId = 999L;

        given()
            .pathParam("userId", inexistentUserId)
            .queryParam("followerId", followerId)
        .when()
            .delete()
        .then()
            .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should unfollow an user")
    void unfollowUserTest() {
        given()
            .pathParam("userId", userId)
            .queryParam("followerId", followerId)
        .when()
            .delete()
        .then()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }
}