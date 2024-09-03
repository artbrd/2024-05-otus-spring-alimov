package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.OtusRoleDto;
import ru.otus.hw.dto.OtusUserDto;
import ru.otus.hw.models.OtusUser;

@Component
@RequiredArgsConstructor
public class OtusUserConverter {

    private final OtusRoleConverter otusRoleConverter;

    public OtusUserDto toDto(OtusUser otusUser) {
        OtusUserDto otusUserDto = new OtusUserDto();
        otusUserDto.setId(otusUser.getId());
        otusUserDto.setUsername(otusUser.getUsername());
        otusUserDto.setPassword(otusUser.getPassword());
        otusUserDto.setRole(otusUser.getRoles()
                .stream()
                .map(otusRoleConverter::toDto)
                .toList());
        return otusUserDto;
    }

    public UserDetails toUserDetails(OtusUserDto otusUserDto) {
        var roles = otusUserDto.getRole()
                .stream()
                .map(OtusRoleDto::getName)
                .toArray(String[]::new);
        return User.builder()
                .username(otusUserDto.getUsername())
                .password(otusUserDto.getPassword())
                .roles(roles)
                .build();
    }
}
