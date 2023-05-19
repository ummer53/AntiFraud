package antifraud.service;

import antifraud.dto.TransactionInDto;
import antifraud.exceptions.AmountIsNegativeException;
import antifraud.exceptions.TransactionInvalidRegionException;
import antifraud.model.Transactions;
import antifraud.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionService {
    public enum TransactionStatus {
        ALLOWED,
        MANUAL_PROCESSING,
        PROHIBITED
    }

    public List<String> regions = Arrays.asList("EAP", "ECA", "HIC", "LAC", "MENA", "SA", "SSA");

    final SuspiciousIpService suspiciousIpService;
    final StolenCardService stolenCardService;
    final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(SuspiciousIpService suspiciousIpService, StolenCardService stolenCardService, TransactionRepository transactionRepository) {
        this.suspiciousIpService = suspiciousIpService;
        this.stolenCardService = stolenCardService;
        this.transactionRepository = transactionRepository;
    }

    public void validateRegions(String name) throws TransactionInvalidRegionException {
        if (!regions.contains(name)) {
            throw new TransactionInvalidRegionException();
        }
    }

    public List<Transactions> getLastHourTransactions(String number, LocalDateTime time) {
        return transactionRepository.findByNumberAndDateBetween(number, time.minusHours(1), time);
    }

    public List<String> getStatement(TransactionInDto t) {
        validateRegions(t.region());
        suspiciousIpService.validateIpFormat(t.ip());
        stolenCardService.validateCardNumber(t.number());

        List<String> lastCalledIps = new ArrayList<>();
        List<String> lastCallsRegions = new ArrayList<>();

        for (Transactions transaction : getLastHourTransactions(t.number(), t.date())) {
            if (!lastCallsRegions.contains(transaction.getRegion()) && !Objects.equals(transaction.getRegion(), t.region())) {
                lastCallsRegions.add(transaction.getRegion());
            }

            if (!lastCalledIps.contains(transaction.getIp())  && !Objects.equals(transaction.getIp(), t.ip())) {
                lastCalledIps.add(transaction.getIp());
            }
        }

        StatementResult statementResult = new StatementResult();

        if (suspiciousIpService.ipExists(t.ip())) {
            statementResult.addProhibited("ip");
        }

        if (stolenCardService.numberExists(t.number())) {
            statementResult.addProhibited("card-number");
        }

        statementResult.validateCorrelation(lastCallsRegions.size(), "region-correlation");
        statementResult.validateCorrelation(lastCalledIps.size(), "ip-correlation");

        if (t.amount() <= 0) {
            throw new AmountIsNegativeException();
        } else if (t.amount() <= 200) {
            statementResult.addAllowed("none");
        } else if (t.amount() <= 1500) {
            statementResult.addManualProcessing("amount");
        } else {
            statementResult.addProhibited("amount");
        }

        transactionRepository.save(t.toEntity());

        return statementResult.getResult();
    }

    private static class StatementResult {
        HashMap<String, List<String>> map = new HashMap<>();
        String allowedName = TransactionStatus.ALLOWED.name();
        String manualProcessingName = TransactionStatus.MANUAL_PROCESSING.name();
        String prohibitedName = TransactionStatus.PROHIBITED.name();

        public void validateCorrelation(int size, String name) {
            if (size == 2) {
                addManualProcessing(name);
            } else if (size > 2) {
                addProhibited(name);
            }
        }

        public void addAllowed(String value) {
            addIntoMap(allowedName, value);
        }

        public void addManualProcessing(String value) {
            addIntoMap(manualProcessingName, value);
        }

        public void addProhibited(String value) {
            addIntoMap(prohibitedName, value);
        }

        public List<String> getResult() {
            if (map.containsKey(prohibitedName)) {
                return getResult(prohibitedName);
            } else if (map.containsKey(manualProcessingName)) {
                return getResult(manualProcessingName);
            }
            return getResult(allowedName);
        }

        private List<String> getResult(String name) {
            List<String> list = new ArrayList<>();
            list.add(name);
            Collections.sort(map.get(name));
            list.add(String.join(", ", map.get(name)));
            return list;
        }

        private void addIntoMap(String name, String value) {
            if (!map.containsKey(name)) {
                map.put(name, new ArrayList<>());
            }
            map.get(name).add(value);
        }
    }
}
