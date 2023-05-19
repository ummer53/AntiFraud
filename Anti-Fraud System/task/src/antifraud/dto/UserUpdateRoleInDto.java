package antifraud.dto;

import javax.validation.constraints.NotBlank;

public record UserUpdateRoleInDto(
        @NotBlank String username,
        @NotBlank String role
) {
}


