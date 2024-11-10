package ru.clevertec.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.exception.handling.exception.InformationChangeStatusUserException;
import ru.clevertec.exception.handling.exception.NoSuchUserEmailException;
import ru.clevertec.exception.handling.exception.NotFoundException;
import ru.clevertec.userservice.dto.request.UserRegisterRequest;
import ru.clevertec.userservice.dto.request.UserUpdateRequest;
import ru.clevertec.userservice.dto.response.UserResponse;
import ru.clevertec.userservice.enums.Status;
import ru.clevertec.userservice.enums.error_response.ErrorMessage;
import ru.clevertec.userservice.mapper.UserMapper;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.repository.UserRepository;
import ru.clevertec.userservice.service.UserIdentityService;
import ru.clevertec.userservice.service.UserService;
import ru.clevertec.userservice.util.AuthUtil;
import ru.clevertec.userservice.validation.UserValidator;

import java.util.UUID;

import static ru.clevertec.userservice.util.Constants.ROLE_JOURNALIST;
import static ru.clevertec.userservice.util.Constants.ROLE_SUBSCRIBER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserValidator userValidator;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final UserIdentityService userIdentityService;

    @Override
    @Transactional
    public void registerJournalist(UserRegisterRequest userRegisterRequest) {
        userValidator.checkUniqueEmail(userRegisterRequest.getEmail());

        User user = userMapper.fromRequest(userRegisterRequest, Status.ACTIVE, ROLE_JOURNALIST);

        userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void registerSubscriber(UserRegisterRequest userRegisterRequest) {
        userValidator.checkUniqueEmail(userRegisterRequest.getEmail());

        User userToSave = userMapper.fromRequest(userRegisterRequest, Status.ACTIVE, ROLE_SUBSCRIBER);

        userRepository.saveAndFlush(userToSave);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserResponse", key = "#userId")
    public UserResponse findUserById(UUID userId) {
        return userMapper.toResponse(userIdentityService.getUserOrThrowException(userId));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "User", key = "#userEmail")
    public User findActiveUserByEmailOrThrowException(String userEmail) {
        return userRepository.findByEmailAndStatus(userEmail, Status.ACTIVE)
                .orElseThrow(() -> new NoSuchUserEmailException(ErrorMessage.USER_NOT_EXIST.getMessage() + userEmail));
    }


    @Override
    @Transactional
    @CachePut(value = "UserResponse", key = "#userId")
    public UserResponse updateUserById(UUID userId, UserUpdateRequest userUpdateRequest) {
        userValidator.checkUniqueEmail(userUpdateRequest.getEmail(), userId);

        User user = userIdentityService.getUserOrThrowException(userId);

        userMapper.update(user, userUpdateRequest, userId);

        user = userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void deactivateUserById(UUID userId) {
        changeUserStatus(userId, Status.NOT_ACTIVE);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "User", key = "#userId"),
            @CacheEvict(value = "UserResponse", key = "#userId")
    })
    public void deleteUserById(UUID userId) {
        changeUserStatus(userId, Status.DELETED);
    }


    private void changeUserStatus(UUID userId, Status userStatus) {
        User userInDB = userRepository.findById(userId)
                .map(user -> {
                    if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"))) {
                        throw new InformationChangeStatusUserException(ErrorMessage.CHANGE_STATUS.getMessage());
                    }
                    return user;
                })
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage() + userId));
        userInDB.setStatus(userStatus);
        userInDB.setUpdatedBy(AuthUtil.getUserId());
        userRepository.save(userInDB);
    }
}