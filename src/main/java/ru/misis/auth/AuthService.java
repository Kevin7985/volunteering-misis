package ru.misis.auth;

import ru.misis.auth.dto.Authorization;

public interface AuthService {
    Authorization authViaGoogle(String code);
}
