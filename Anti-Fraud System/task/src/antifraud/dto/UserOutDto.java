package antifraud.dto;

import antifraud.model.User;

public record UserOutDto(
        Long id,
        String name,
        String username,
        String role
) {
    public static UserOutDto fromEntity(User user) {
        return new UserOutDto(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getRole()
        );
    }
}
