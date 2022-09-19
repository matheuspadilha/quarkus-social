package io.github.matheuspadilha.quarkussocial.service;

import io.github.matheuspadilha.quarkussocial.domain.model.Post;
import io.github.matheuspadilha.quarkussocial.domain.model.User;
import io.github.matheuspadilha.quarkussocial.domain.repository.PostRepository;
import io.github.matheuspadilha.quarkussocial.resource.dto.CreatePostRequest;
import io.github.matheuspadilha.quarkussocial.resource.dto.PostResponse;
import io.github.matheuspadilha.quarkussocial.resource.exception.PostBadRequestException;
import io.github.matheuspadilha.quarkussocial.resource.exception.PostForbiddenException;
import io.github.matheuspadilha.quarkussocial.resource.exception.UserNotFoundException;
import io.github.matheuspadilha.quarkussocial.utils.Constants;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@ApplicationScoped
public class PostService {

    @Inject
    UserService userService;

    @Inject
    PostRepository repository;

    @Inject
    FollowerService followerService;

    @Transactional
    public Post create(Long userId, CreatePostRequest postRequest) {
        User user = Optional.ofNullable(userService.findById(userId))
                .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND));

        Post post = new Post();
        post.setText(postRequest.getText());
        post.setUser(user);

        repository.persist(post);

        return post;
    }

    public List<PostResponse> findAllByUser(Long userId, Long followerId) {
        if (isNull(followerId)) {
            throw new PostBadRequestException("You forgot the header followerId");
        }

        User user = Optional.ofNullable(userService.findById(userId))
                .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND));
        User follower = Optional.ofNullable(userService.findById(followerId))
                .orElseThrow(() -> new UserNotFoundException(Constants.INEXISTENT_FOLLOWER_ID));

        boolean isFollows = followerService.isFollows(user, follower);
        if(!isFollows) {
            throw new PostForbiddenException("You can't see these posts");
        }

        PanacheQuery<Post> query = repository.find("user", Sort.by("dateTime", Sort.Direction.Descending), user);

        return query.list().stream().map(this::fromEntity).toList();
    }

    private PostResponse fromEntity(Post post) {
        return PostResponse.builder()
                .text(post.getText())
                .dateTime(post.getDataTime())
                .build();
    }
}
