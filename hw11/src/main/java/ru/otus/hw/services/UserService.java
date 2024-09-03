package ru.otus.hw.services;

import ru.otus.hw.dto.OtusUserDto;

import java.util.Optional;

public interface UserService {

    Optional<OtusUserDto> findByUsername(String username);
}
