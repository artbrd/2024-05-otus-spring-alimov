package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.OtusRoleDto;
import ru.otus.hw.models.OtusRole;

@Component
public class OtusRoleConverter {

    public OtusRoleDto toDto(OtusRole otusRole) {
        OtusRoleDto otusRoleDto = new OtusRoleDto();
        otusRoleDto.setId(otusRole.getId());
        otusRoleDto.setName(otusRole.getName());
        return otusRoleDto;
    }
}
