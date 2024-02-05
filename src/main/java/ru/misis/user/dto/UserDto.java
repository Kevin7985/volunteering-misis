package ru.misis.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDto {
    private final UUID id;
    private final String misisId;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String middleName;
}
