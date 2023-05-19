package antifraud.service;

import antifraud.dto.UserAccessInDto;
import antifraud.dto.UserInDto;
import antifraud.dto.UserUpdateRoleInDto;
import antifraud.exceptions.*;
import antifraud.model.User;
import antifraud.repository.UserRepository;
import antifraud.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    public enum AccessOperation {
        LOCK,
        UNLOCK
    }

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    boolean adminIsSet = false;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(UserInDto userDto) throws UserExistsException {
        User user = userDto.toEntity();
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserExistsException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String role = SecurityConfig.Roles.MERCHANT.name();
        boolean active = false;

        if (!adminIsSet) {
            Optional<User> admin = userRepository.findFirstByRole(SecurityConfig.Roles.ADMINISTRATOR.name());
            if (admin.isPresent()) {
                adminIsSet = true;
            } else {
                role = SecurityConfig.Roles.ADMINISTRATOR.name();
                active = true;
            }
        }

        user.setRole(role);
        user.setActive(active);
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    public User delete(String username) throws UserNotFoundException {
        User user = findByUsername(username);
        userRepository.delete(user);
        return user;
    }

    public User findByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public User updateRole(UserUpdateRoleInDto userDto) throws RoleIsSameException, UserNotFoundException, InvalidRoleException, UserSetRoleDeniedException {
        System.out.println(userDto.role());
        if (
                !Objects.equals(userDto.role(), SecurityConfig.Roles.MERCHANT.name()) &&
                        !Objects.equals(userDto.role(), SecurityConfig.Roles.SUPPORT.name())
        ) {
            throw new InvalidRoleException();
        }

        User user = userRepository.findByUsername(userDto.username())
                .orElseThrow(UserNotFoundException::new);

        if (Objects.equals(user.getRole(), SecurityConfig.Roles.ADMINISTRATOR.name())) {
            throw new UserSetRoleDeniedException();
        }

        if (Objects.equals(user.getRole(), userDto.role())) {
            throw new RoleIsSameException();
        }

        user.setRole(userDto.role());

        return userRepository.save(user);
    }

    public void access(UserAccessInDto userDto) throws UserSetAccessDeniedException, UserNotFoundException, InvalidOperationException {
        if (
                !Objects.equals(userDto.operation(), AccessOperation.LOCK.name()) &&
                        !Objects.equals(userDto.operation(), AccessOperation.UNLOCK.name())
        ) {
            throw new InvalidOperationException();
        }

        User user = userRepository.findByUsername(userDto.username())
                .orElseThrow(UserNotFoundException::new);

        if (Objects.equals(user.getRole(), SecurityConfig.Roles.ADMINISTRATOR.name())) {
            throw new UserSetAccessDeniedException();
        }

        user.setActive(Objects.equals(userDto.operation(), AccessOperation.UNLOCK.name()));
        userRepository.save(user);
    }

}
