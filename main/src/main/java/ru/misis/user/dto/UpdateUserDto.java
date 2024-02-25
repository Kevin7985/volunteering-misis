package ru.misis.user.dto;

import lombok.Data;
import ru.misis.skill.dto.SkillDto;
import ru.misis.user.exceptions.UserValidation;

import java.util.List;

@Data
public class UpdateUserDto {
    private String firstName;
    private String lastName;
    private String middleName;
    private String about;
    private List<SkillDto> skills;

    public UpdateUserDto(String firstName, String lastName, String middleName, String about, List<SkillDto> skills) {
        if (firstName != null && (firstName.isBlank() || firstName.length() > 255)) {
            throw new UserValidation("Имя пользователя не может быть пустым или содержать больше 255 символов");
        }
        this.firstName = firstName;

        if (lastName != null && (lastName.isBlank() || lastName.length() > 255)) {
            throw new UserValidation("Фамилия пользователя не может быть пустым или содержать больше 255 символов");
        }
        this.lastName = lastName;

        this.middleName = middleName;

        if (about != null && (about.length() > 5000)) {
            throw new UserValidation("Описание пользователя не может содержать больше 5000 символов");
        }
        this.about = about;
        this.skills = skills;
    }
}
