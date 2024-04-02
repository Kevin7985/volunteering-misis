package ru.misis.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.misis.error.exceptions.Forbidden;
import ru.misis.service.MapperService;
import ru.misis.service.ValidationService;
import ru.misis.skill.SkillRepository;
import ru.misis.skill.dto.SkillDto;
import ru.misis.skill.model.Skill;
import ru.misis.user.dto.NewUserDto;
import ru.misis.user.dto.UpdateUserDto;
import ru.misis.user.dto.UserDto;
import ru.misis.user.exceptions.UserAlreadyExists;
import ru.misis.user.model.User;
import ru.misis.utils.Pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final MapperService mapperService;
    private final ValidationService validationService;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @Override
    public UserDto createUser(NewUserDto userDto) {
        Optional<User> foundByEmail = userRepository.findByEmail(userDto.getEmail());
        if (foundByEmail.isPresent()) {
            throw new UserAlreadyExists("Пользователь с данной почтой уже существует");
        }

        if (userDto.getMisisId() != null) {
            Optional<User> foundByMisisId = userRepository.findByMisisId(userDto.getMisisId());
            if (foundByMisisId.isPresent()) {
                throw new UserAlreadyExists("Данный аккаунт МИСиС уже привязан к профилю");
            }
        }

        User user = userRepository.save(mapperService.toUser(userDto));

        log.info("Создан новый пользователь: " + user);
        return mapperService.toUserDto(user);
    }

    @Override
    public List<UserDto> findUsers(String email, Integer from, Integer size) {
        List<UserDto> usersList = new ArrayList<>();

        Pageable pageable;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Page<User> page;
        Pagination pager = new Pagination(from, size);

        for (int i = pager.getPageStart(); i < pager.getPagesAmount(); i++) {
            pageable = PageRequest.of(i, pager.getPageSize(), sort);
            page = email != null ? userRepository.searchUsersByEmail(email, pageable) : userRepository.findAll(pageable);
            usersList.addAll(page.stream()
                    .map(mapperService::toUserDto)
                    .toList());
        }

        log.info(String.format("Поиск пользователей (email = %s, from = %d, size = %d)", email, from, size));
        return usersList.stream().limit(size).toList();
    }

    @Override
    public UserDto getUserById(UUID id) {
        User user = validationService.validateUser(id);

        log.info("Получение пользователя по id = " + id);
        return mapperService.toUserDto(user);
    }

    @Override
    public UserDto updateUserById(Authentication auth, UUID id, UpdateUserDto userDto) {
        User user = validationService.validateUser(id);

        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STAFF"))) {
            if (!user.getId().equals(UUID.fromString(auth.getName()))) {
                throw new Forbidden();
            }
        }

        user.setFirstName(userDto.getFirstName() == null ? user.getFirstName() : userDto.getFirstName());
        user.setLastName(userDto.getLastName() == null ? user.getLastName() : userDto.getLastName());
        user.setMiddleName(userDto.getMiddleName() == null ? user.getMiddleName() : userDto.getMiddleName());
        user.setAbout(userDto.getAbout() == null ? user.getAbout() : userDto.getAbout());

        if (userDto.getSkills() != null) {
            List<UUID> skillIds = userDto.getSkills().stream().map(SkillDto::getId).toList();
            List <Skill> skills = skillRepository.findAllById(skillIds);

            user.setSkills(skills);
        }

        log.info("Обновление данных пользователя (id = " + id + "): " + userDto);
        return mapperService.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUserById(Authentication auth, UUID id) {
        User user = validationService.validateUser(id);

        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STAFF"))) {
            if (!user.getId().equals(UUID.fromString(auth.getName()))) {
                throw new Forbidden();
            }
        }

        log.info("Удаление пользователя по id = " + id);
        userRepository.deleteById(id);
    }
}
