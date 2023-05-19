package antifraud.dto;

import antifraud.model.User;

import javax.validation.constraints.NotBlank;

public record UserInDto(
        @NotBlank String name,
        @NotBlank String username,
        @NotBlank String password
) {
    public User toEntity(){
        return new User(name, username, password);
    }
}
