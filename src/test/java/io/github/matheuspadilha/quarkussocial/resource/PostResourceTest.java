package io.github.matheuspadilha.quarkussocial.resource;

import io.github.matheuspadilha.quarkussocial.domain.model.Follower;
import io.github.matheuspadilha.quarkussocial.domain.model.Post;
import io.github.matheuspadilha.quarkussocial.domain.model.User;
import io.github.matheuspadilha.quarkussocial.domain.repository.FollowerRepository;
import io.github.matheuspadilha.quarkussocial.domain.repository.PostRepository;
import io.github.matheuspadilha.quarkussocial.domain.repository.UserRepository;
import io.github.matheuspadilha.quarkussocial.resource.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
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

    @Inject
    FollowerRepository followerRepository;

    @Inject
    PostRepository postRepository;

    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;

    @BeforeEach
    @Transactional
    void setUp() {
        // usuario padrao dos testes
        User user = new User();
        user.setAge(27);
        user.setName("John Doe");
        userRepository.persist(user);
        userId = user.getId();

        // usuario que nao segue ninguem
        User userNotFollower = new User();
        userNotFollower.setAge(21);
        userNotFollower.setName("Lorem");
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        // usuario seguidor
        User userFollower = new User();
        userFollower.setAge(30);
        userFollower.setName("Ipsum");
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        // registro de seguidor
        Follower follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);

        // criado a postagem para o usuario
        Post post = new Post();
        post.setText("Hello world!");
        post.setUser(user);
        postRepository.persist(post);
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

    @Test
    @DisplayName("Should return 400 when followerId header is not present")
    void listPostFollowerHeaderNotSendTest() {
        given()
            .pathParam("userId", userId)
        .when()
            .get()
        .then()
            .statusCode(400)
            .body(Matchers.is("You forgot the header followerId"));
    }

    @Test
    @DisplayName("Should return 404 when user doesn't exist")
    void listPostUserNotFoundTest() {
        Long inexistentUserId = 999L;

        given()
            .pathParam("userId", inexistentUserId)
            .header("followerId", 123L)
        .when()
            .get()
        .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("Should return 404 when follower doesn't exist")
    void listPostFollowerNotFoundTest() {
        Long inexistentFollowerId = 999L;

        given()
            .pathParam("userId", userId)
            .header("followerId", inexistentFollowerId)
        .when()
            .get()
        .then()
            .statusCode(404)
            .body(Matchers.is("Inexistent followerId."));
    }

    @Test
    @DisplayName("Should return 403 when follower isn't a follower")
    void listPostNotAFollowerTest() {
        given()
            .pathParam("userId", userId)
            .header("followerId", userNotFollowerId)
        .when()
            .get()
        .then()
            .statusCode(403)
            .body(Matchers.is("You can't see these posts"));
    }

    @Test
    @DisplayName("Should return posts")
    void listPostTest() {
        given()
            .pathParam("userId", userId)
            .header("followerId", userFollowerId)
        .when()
            .get()
        .then()
            .statusCode(200)
            .body("size()", Matchers.is(1));
    }
}