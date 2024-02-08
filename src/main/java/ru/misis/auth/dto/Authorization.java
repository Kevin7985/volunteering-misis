package ru.misis.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Authorization {
    private final UUID userId;
    private final String token;
    private final Integer expires;
}
