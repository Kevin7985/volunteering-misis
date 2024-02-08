package ru.misis.auth.google.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleCredentials {
    private final String access_token;
    private final Integer expires_in;
    private final String scope;
    private final String token_type;
    private final String id_token;
}
