package ru.misis.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.misis.skill.dto.SkillDto;

import java.util.List;
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
    private final String about;
    private final Boolean isModerator;
    private final Boolean isStaff;
    private final List<SkillDto> skills;
}
