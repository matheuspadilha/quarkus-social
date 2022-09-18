package io.github.matheuspadilha.quarkussocial.service;

import io.github.matheuspadilha.quarkussocial.domain.model.Post;
import io.github.matheuspadilha.quarkussocial.domain.model.User;
import io.github.matheuspadilha.quarkussocial.domain.repository.PostRepository;
import io.github.matheuspadilha.quarkussocial.resource.dto.CreatePostRequest;
import io.github.matheuspadilha.quarkussocial.resource.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class PostService {

    @Inject
    UserService userService;

    @Inject
    PostRepository repository;

    @Transactional
    public Post create(Long userId, CreatePostRequest postRequest) {
        User user = userService.findById(userId);

        Post post = new Post();
        post.setText(postRequest.getText());
        post.setUser(user);

        repository.persist(post);

        return post;
    }

    public List<PostResponse> findAllByUser(Long userId) {
        User user = userService.findById(userId);

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
