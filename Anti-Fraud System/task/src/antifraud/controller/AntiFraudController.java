package antifraud.controller;

import antifraud.dto.*;
import antifraud.service.StolenCardService;
import antifraud.service.SuspiciousIpService;
import antifraud.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RestController
@RequestMapping(path = "api/antifraud/")
@Validated
public class AntiFraudController {

    final TransactionService transactionService;
    final SuspiciousIpService suspiciousIpService;
    final StolenCardService stolenCardService;

    @Autowired
    public AntiFraudController(TransactionService transactionService, SuspiciousIpService suspiciousIpService, StolenCardService stolenCardService) {
        this.transactionService = transactionService;
        this.suspiciousIpService = suspiciousIpService;
        this.stolenCardService = stolenCardService;
    }

    @PostMapping("transaction")
    public TransactionOutDto transaction(@Valid @RequestBody TransactionInDto t) {
        List<String> list = transactionService.getStatement(t);
        return new TransactionOutDto(list.get(0), list.get(1));
    }

    @PostMapping("suspicious-ip")
    public SuspiciousIpOutDto createSuspiciousIp(@RequestBody SuspiciousIpInDto dataIn) {
        suspiciousIpService.validateIpFormat(dataIn.ip());
        return SuspiciousIpOutDto.fromEntity(suspiciousIpService.create(dataIn));
    }

    @DeleteMapping("suspicious-ip/{ip}")
    public SuspiciousIpDeleteOutDto deleteSuspiciousIp(@PathVariable(name = "ip") String ip) {
        suspiciousIpService.validateIpFormat(ip);
        suspiciousIpService.delete(ip);
        return SuspiciousIpDeleteOutDto.success(ip);
    }

    @GetMapping("suspicious-ip")
    public List<SuspiciousIpGetOutDto> getSuspiciousIp() {
        return suspiciousIpService.findAll().stream().map(SuspiciousIpGetOutDto::fromEntity).toList();
    }

    @PostMapping("stolencard")
    public StolenCardCreateOutDto createStolenCard(@Valid @RequestBody StolenCardCreateInDto dataIn) {
        stolenCardService.validateCardNumber(dataIn.number());
        return StolenCardCreateOutDto.fromEntity(stolenCardService.create(dataIn));
    }


    @DeleteMapping("stolencard/{number}")
    public StolenCardDeleteOutDto deleteStolenCard(@PathVariable(name = "number") String number) {
        stolenCardService.validateCardNumber(number);
        stolenCardService.delete(number);
        return StolenCardDeleteOutDto.success(number);
    }

    @GetMapping("stolencard")
    public List<StolenCardGetOutDto> getStolenCards() {
        return stolenCardService.findAll().stream().map(StolenCardGetOutDto::fromEntity).toList();
    }
}
