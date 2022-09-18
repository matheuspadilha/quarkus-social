package io.github.matheuspadilha.quarkussocial.resource.dto;

import io.github.matheuspadilha.quarkussocial.domain.model.Follower;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowerResponse {
    private Long id;
    private String name;

    public FollowerResponse(Follower follower) {
        this(follower.getId(), follower.getFollower().getName());
    }
}
