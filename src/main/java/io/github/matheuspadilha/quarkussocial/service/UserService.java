package io.github.matheuspadilha.quarkussocial.service;

import io.github.matheuspadilha.quarkussocial.domain.model.User;
import io.github.matheuspadilha.quarkussocial.domain.repository.UserRepository;
import io.github.matheuspadilha.quarkussocial.resource.dto.CreateUserRequest;
import io.github.matheuspadilha.quarkussocial.resource.exception.UserNotFoundException;
import io.github.matheuspadilha.quarkussocial.utils.Constants;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository repository;

    @Transactional
    public User create(CreateUserRequest userRequest) {
        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        repository.persist(user);

        return user;
    }

    public List<User> findAll() {
        PanacheQuery<User> query = repository.findAll();

        return query.list();
    }

    @Transactional
    public void delete(Long id) {
        User user = Optional.ofNullable(findById(id))
                .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND));

        repository.delete(user);
    }

    @Transactional
    public void update(Long id, CreateUserRequest userData) {
        User user = Optional.ofNullable(findById(id))
                .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND));

        user.setName(userData.getName());
        user.setAge(userData.getAge());
    }

    public User findById(Long id) {
        return repository.findById(id);
    }
}
