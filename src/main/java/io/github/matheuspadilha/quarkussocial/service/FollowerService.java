package io.github.matheuspadilha.quarkussocial.service;

import io.github.matheuspadilha.quarkussocial.domain.model.Follower;
import io.github.matheuspadilha.quarkussocial.domain.model.User;
import io.github.matheuspadilha.quarkussocial.domain.repository.FollowerRepository;
import io.github.matheuspadilha.quarkussocial.resource.dto.FollowerRequest;
import io.github.matheuspadilha.quarkussocial.resource.dto.FollowerResponse;
import io.github.matheuspadilha.quarkussocial.resource.dto.FollowersPerUserResponse;
import io.github.matheuspadilha.quarkussocial.resource.exception.FollowerNotFoundException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class FollowerService {

    @Inject
    UserService userService;

    @Inject
    FollowerRepository followerRepository;

    @Transactional
    public void followUser(Long userId, FollowerRequest followerRequest) {

        if (userId.equals(followerRequest.getFollowerId())) {
            throw new FollowerNotFoundException("You can't follow yourself");
        }

        User user = userService.findById(userId);
        User follower = userService.findById(followerRequest.getFollowerId());

        boolean isFollows = followerRepository.followers(follower, user);

        if (!isFollows) {
            Follower followerEntity = new Follower();
            followerEntity.setUser(user);
            followerEntity.setFollower(follower);

            followerRepository.persist(followerEntity);
        }
    }

    public FollowersPerUserResponse findByUser(Long userId) {
        User user = userService.findById(userId);

        List<Follower> followers = followerRepository.findByUser(user);
        List<FollowerResponse> followerResponses = followers.stream().map(FollowerResponse::new).toList();

        return FollowersPerUserResponse.builder()
                .followersCount(followers.size())
                .content(followerResponses)
                .build();
    }

    @Transactional
    public void unfollowUser(Long userId, Long followerId) {
        User user = userService.findById(userId);
        User follower = userService.findById(followerId);

        followerRepository.deleteByFollowerAndUser(follower.getId(), user.getId());
    }
}
