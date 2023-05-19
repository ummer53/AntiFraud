package antifraud.dto;

import antifraud.model.SuspiciousIp;

import javax.validation.constraints.NotBlank;

public record SuspiciousIpInDto(
        @NotBlank
        String ip
) {
    public SuspiciousIp toEntity(){
        return new SuspiciousIp(ip);
    }
}
