package ru.misis.auth.google.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleProfile {
    private final String id;
    private final String email;
    private final String name;
    private final String given_name;
    private final String family_name;
    private final String picture;
}
