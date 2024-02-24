package ru.misis.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.misis.auth.dto.Authorization;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Методы для получения токена авторизации")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/google")
    @Operation(summary = "Авторизация с помощью Google")
    public Authorization viaGoogle(
            @RequestParam(required = true) String idToken
    ) {
        return authService.authViaGoogle(idToken);
    }
}