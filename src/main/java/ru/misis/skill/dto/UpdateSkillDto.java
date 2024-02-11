package ru.misis.skill.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.misis.skill.exceptions.SkillValidation;

@Data
public class UpdateSkillDto {
    private final String title;

    public UpdateSkillDto(@JsonProperty("title") String title) {
        if (title != null && (title.length() < 4 || title.length() > 255)) {
            throw new SkillValidation("Название навыка не может быть меньше 4 символов и больше 255");
        }
        this.title = title;
    }
}
