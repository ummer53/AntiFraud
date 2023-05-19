package antifraud.dto;

import javax.validation.constraints.NotBlank;

public record UserAccessInDto(
        @NotBlank String username,
        @NotBlank String operation
) {
}
