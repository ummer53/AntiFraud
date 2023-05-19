package antifraud.dto;

import antifraud.model.StolenCard;

public record StolenCardCreateOutDto(
        Long id,
        String number
) {
    public static StolenCardCreateOutDto fromEntity(StolenCard stolenCard) {
        return new StolenCardCreateOutDto(stolenCard.getId(), stolenCard.getNumber());
    }
}
