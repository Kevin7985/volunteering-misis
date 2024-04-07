package ru.misis.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.misis.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE lower(u.email) LIKE lower(concat('%', ?1, '%'))")
    Page<User> searchUsersByEmail(String text, Pageable pageable);

    @Query("SELECT u FROM User u WHERE lower(u.email) LIKE lower(concat('%', ?1, '%'))")
    List<User> searchUsersByEmail(String text);

    Optional<User> findByEmail(String email);

    Optional<User> findByMisisId(String misisId);
}
