package ru.misis.skills.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SkillDto {
    private final UUID id;
    private final String title;
}
