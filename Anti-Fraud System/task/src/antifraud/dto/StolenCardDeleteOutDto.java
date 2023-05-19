package antifraud.dto;

public record StolenCardDeleteOutDto(
        String status
) {
    public static StolenCardDeleteOutDto success(String number) {
        return new StolenCardDeleteOutDto("Card "+number+" successfully removed!");
    }
}
