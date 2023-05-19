package antifraud.dto;

public record SuspiciousIpDeleteOutDto(
        String status
) {
    public static SuspiciousIpDeleteOutDto success(String ip) {
        return new SuspiciousIpDeleteOutDto("IP " + ip + " successfully removed!");
    }
}
