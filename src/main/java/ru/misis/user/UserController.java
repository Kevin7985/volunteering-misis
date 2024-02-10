package ru.misis.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.misis.user.dto.NewUserDto;
import ru.misis.user.dto.UpdateUserDto;
import ru.misis.user.dto.UserDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@RequiredArgsConstructor
@Tag(name = "Users", description = "Методы для работы с пользователями")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание нового пользователя")
    @SecurityRequirement(name = "Bearer Authentication")
    public UserDto createUser(@RequestBody @Valid NewUserDto newUserDto) {
        return userService.createUser(newUserDto);
    }

    @GetMapping
    @Operation(summary = "Поиск пользователей")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<UserDto> findUsers(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "20") @Min(1) Integer size) {
        return userService.findUsers(email, from, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя по идентификатору")
    @SecurityRequirement(name = "Bearer Authentication")
    public UserDto getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление данных пользователя по идентификатору")
    @SecurityRequirement(name = "Bearer Authentication")
    public UserDto updateUserById(@PathVariable UUID id, @RequestBody UpdateUserDto userDto) {
        return userService.updateUserById(id, userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаление пользователя по идентификатору")
    @SecurityRequirement(name = "Bearer Authentication")
    public void deleteUserById(@PathVariable UUID id) {
        userService.deleteUserById(id);
    }
}
