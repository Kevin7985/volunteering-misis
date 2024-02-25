package ru.misis.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.misis.auth.dto.Authorization;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Методы для получения токена авторизации")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/google")
    @Operation(summary = "Авторизация с помощью Google")
    public Authorization viaGoogle(
            @RequestParam(required = true) String idToken
    ) {
        return authService.authViaGoogle(idToken);
    }
}