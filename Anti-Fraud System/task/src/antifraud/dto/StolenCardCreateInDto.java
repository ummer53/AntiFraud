package antifraud.dto;

import antifraud.model.StolenCard;

import javax.validation.constraints.NotBlank;

public record StolenCardCreateInDto(
        @NotBlank String number
) {
    public StolenCard toEntity(){
        return new StolenCard(number);
    }
}
