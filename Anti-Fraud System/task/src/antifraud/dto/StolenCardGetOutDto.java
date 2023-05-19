package antifraud.dto;

import antifraud.model.StolenCard;

public record StolenCardGetOutDto(
        Long id,
        String number
) {
    public static StolenCardGetOutDto fromEntity(StolenCard stolenCard){
        return new StolenCardGetOutDto(stolenCard.getId(), stolenCard.getNumber());
    }
}
