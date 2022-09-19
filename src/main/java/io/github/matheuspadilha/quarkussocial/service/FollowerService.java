package io.github.matheuspadilha.quarkussocial.service;

import io.github.matheuspadilha.quarkussocial.domain.model.Follower;
import io.github.matheuspadilha.quarkussocial.domain.model.User;
import io.github.matheuspadilha.quarkussocial.domain.repository.FollowerRepository;
import io.github.matheuspadilha.quarkussocial.resource.dto.FollowerRequest;
import io.github.matheuspadilha.quarkussocial.resource.dto.FollowerResponse;
import io.github.matheuspadilha.quarkussocial.resource.dto.FollowersPerUserResponse;
import io.github.matheuspadilha.quarkussocial.resource.exception.FollowerAndUserConflictException;
import io.github.matheuspadilha.quarkussocial.resource.exception.UserNotFoundException;
import io.github.matheuspadilha.quarkussocial.utils.Constants;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FollowerService {

    @Inject
    UserService userService;

    @Inject
    FollowerRepository followerRepository;

    @Transactional
    public void followUser(Long userId, FollowerRequest followerRequest) {

        if (userId.equals(followerRequest.getFollowerId())) {
            throw new FollowerAndUserConflictException("You can't follow yourself");
        }

        User user = Optional.ofNullable(userService.findById(userId))
                .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND));
        User follower = Optional.ofNullable(userService.findById(followerRequest.getFollowerId()))
                .orElseThrow(() -> new UserNotFoundException(Constants.INEXISTENT_FOLLOWER_ID));

        boolean isFollows = isFollows(user, follower);

        if (!isFollows) {
            Follower followerEntity = new Follower();
            followerEntity.setUser(user);
            followerEntity.setFollower(follower);

            followerRepository.persist(followerEntity);
        }
    }

    public boolean isFollows(User user, User follower) {
        return followerRepository.followers(follower, user);
    }

    public FollowersPerUserResponse findByUser(Long userId) {
        User user = Optional.ofNullable(userService.findById(userId))
                .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND));

        List<Follower> followers = followerRepository.findByUser(user);
        List<FollowerResponse> followerResponses = followers.stream().map(FollowerResponse::new).toList();

        return FollowersPerUserResponse.builder()
                .followersCount(followers.size())
                .content(followerResponses)
                .build();
    }

    @Transactional
    public void unfollowUser(Long userId, Long followerId) {
        User user = Optional.ofNullable(userService.findById(userId))
                .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND));
        User follower = Optional.ofNullable(userService.findById(followerId))
                .orElseThrow(() -> new UserNotFoundException(Constants.INEXISTENT_FOLLOWER_ID));

        followerRepository.deleteByFollowerAndUser(follower.getId(), user.getId());
    }
}
