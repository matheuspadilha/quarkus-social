package io.github.matheuspadilha.quarkussocial.resource.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowersPerUserResponse {
    private Integer followersCount;
    private List<FollowerResponse> content;
}
