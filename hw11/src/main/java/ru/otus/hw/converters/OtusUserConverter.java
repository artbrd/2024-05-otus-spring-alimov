package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.OtusRole;
import ru.otus.hw.models.OtusUser;

@Component
@RequiredArgsConstructor
public class OtusUserConverter {

    public UserDetails toUserDetails(OtusUser otusUser) {
        var roles = otusUser.getRoles()
                .stream()
                .map(OtusRole::getName)
                .toArray(String[]::new);
        return User.builder()
                .username(otusUser.getUsername())
                .password(otusUser.getPassword())
                .roles(roles)
                .build();
    }
}
