package antifraud.dto;

import antifraud.model.User;

public record UserUpdateRoleOutDto(
        Long id,
        String name,
        String username,
        String role
) {
    public static UserUpdateRoleOutDto fromEntity(User user) {
        return new UserUpdateRoleOutDto(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getRole()
        );
    }
}
