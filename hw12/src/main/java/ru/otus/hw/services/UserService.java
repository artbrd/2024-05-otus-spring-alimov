package ru.otus.hw.services;

import ru.otus.hw.models.OtusUser;

import java.util.Optional;

public interface UserService {

    Optional<OtusUser> findByUsername(String username);
}
