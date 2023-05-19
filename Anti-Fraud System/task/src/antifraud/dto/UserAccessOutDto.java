package antifraud.dto;

import antifraud.service.UserService;

import java.util.Objects;

public record UserAccessOutDto(
        String status
) {
    public static UserAccessOutDto fromEntity(UserAccessInDto userDto) {
        String operation;
        if (Objects.equals(userDto.operation(), UserService.AccessOperation.LOCK.name())) {
            operation = "locked";
        } else {
            operation = "unlocked";
        }

        return new UserAccessOutDto("User " + userDto.username() + " " + operation + "!");
    }
}
