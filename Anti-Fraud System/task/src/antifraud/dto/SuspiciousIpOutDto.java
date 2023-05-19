package antifraud.dto;

import antifraud.model.SuspiciousIp;

public record SuspiciousIpOutDto (
        Long id,
        String ip
){
    public static SuspiciousIpOutDto fromEntity(SuspiciousIp ip){
        return new SuspiciousIpOutDto(ip.getId(), ip.getIp());
    }
}
