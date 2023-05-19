package antifraud.controller;

import antifraud.dto.*;
import antifraud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Controller
@RequestMapping(path = "api/auth/")
@Validated
public class UserController {
    final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserOutDto createUser(@Valid @RequestBody UserInDto user) {
        return UserOutDto.fromEntity(userService.create(user));
    }

    @GetMapping("list")
    public List<UserOutDto> getList() {
        return userService.findAll().
                stream().
                map(UserOutDto::fromEntity).
                toList();
    }

    @DeleteMapping("user/{username}")
    public UserDeleteOutDto delete(@PathVariable("username") String username) {
        return UserDeleteOutDto.fromEntity(userService.delete(username));
    }

    @PutMapping("role")
    public UserUpdateRoleOutDto updateRole(@Valid @RequestBody UserUpdateRoleInDto userDto) {
        return UserUpdateRoleOutDto.fromEntity(userService.updateRole(userDto));
    }

    @PutMapping("access")
    public UserAccessOutDto access(@Valid @RequestBody UserAccessInDto userDto) {
        userService.access(userDto);
        return UserAccessOutDto.fromEntity(userDto);
    }

}
