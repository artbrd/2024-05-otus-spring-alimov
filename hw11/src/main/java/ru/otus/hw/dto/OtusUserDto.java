package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtusUserDto implements Serializable {
    private long id;

    private String username;

    private String password;

    private List<OtusRoleDto> role;
}