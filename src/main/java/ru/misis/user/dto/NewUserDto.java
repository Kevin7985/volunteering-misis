package ru.misis.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewUserDto {
    private final String misisId;

    @Email(message = "Некорректный формат электронной почты")
    private final String email;

    @NotBlank
    @Size(min = 1, max = 255)
    private final String firstName;

    @NotBlank
    @Size(min = 1, max = 255)
    private final String lastName;

    private final String middleName;
}
