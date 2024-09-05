package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.OtusUserConverter;
import ru.otus.hw.models.OtusUser;
import ru.otus.hw.repositories.OtusUserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final OtusUserRepository otusUserRepository;

    private final OtusUserConverter otusUserConverter;

    @Transactional(readOnly = true)
    @Override
    public Optional<OtusUser> findByUsername(String username) {
        return otusUserRepository.findByUsername(username);
    }
}
