package io.github.matheuspadilha.quarkussocial.domain.repository;

import io.github.matheuspadilha.quarkussocial.domain.model.Follower;
import io.github.matheuspadilha.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean followers(User follower, User user) {
        Map<String, Object> params = Parameters.with("follower", follower).and("user", user).map();

        PanacheQuery<Follower> query = find("follower =:follower and user =:user", params);

        return query.firstResultOptional().isPresent();
    }

    public List<Follower> findByUser(User user) {
        PanacheQuery<Follower> query = find("user", user);

        return query.list();
    }

    public void deleteByFollowerAndUser(Long followerId, Long userId) {
        Map<String, Object> params = Parameters.with("followerId", followerId).and("userId", userId).map();
        delete("follower.id =:followerId and user.id =:userId", params);
    }
}
