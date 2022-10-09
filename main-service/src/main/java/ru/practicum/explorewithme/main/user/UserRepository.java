package ru.practicum.explorewithme.main.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explorewithme.main.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    @Query(value = "" +
            "SELECT * " +
            "FROM users AS u " +
            "WHERE u.id in :ids /*:pageable*/", nativeQuery = true)
    Page<User> findAllByIds(List<Long> ids, Pageable pageable);
}
