package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.OtusUser;

import java.util.Optional;

public interface OtusUserRepository extends JpaRepository<OtusUser, Long> {

    Optional<OtusUser> findByUsername(String username);
}