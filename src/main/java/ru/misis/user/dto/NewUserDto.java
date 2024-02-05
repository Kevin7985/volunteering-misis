package ru.misis.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewUserDto {
    private final String misisId;

    @Email(message = "Некорректный формат электронной почты")
    private final String email;

    private final String firstName;
    private final String lastName;
    private final String middleName;
}
