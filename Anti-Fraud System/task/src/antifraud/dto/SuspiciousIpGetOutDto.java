package antifraud.dto;

import antifraud.model.SuspiciousIp;

public record SuspiciousIpGetOutDto(
        Long id,
        String ip
) {
    public static SuspiciousIpGetOutDto fromEntity(SuspiciousIp suspiciousIp) {
        return new SuspiciousIpGetOutDto(suspiciousIp.getId(), suspiciousIp.getIp());
    }
}
