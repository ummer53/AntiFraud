package antifraud.dto;

import antifraud.model.User;

public record UserDeleteOutDto(
        String username,
        String status
) {
    public static UserDeleteOutDto fromEntity(User user) {
        return new UserDeleteOutDto(
                user.getUsername(),
                "Deleted successfully!"
        );
    }
}
