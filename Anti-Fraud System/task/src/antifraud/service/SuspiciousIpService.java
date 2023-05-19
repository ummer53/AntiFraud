package antifraud.service;

import antifraud.dto.SuspiciousIpInDto;
import antifraud.exceptions.SuspiciousIpDontExistsException;
import antifraud.exceptions.SuspiciousIpExistsException;
import antifraud.exceptions.SuspiciousIpInvalidFormatException;
import antifraud.model.SuspiciousIp;
import antifraud.repository.SuspiciousIpRepository;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuspiciousIpService {
    final SuspiciousIpRepository suspiciousIpRepository;

    @Autowired
    public SuspiciousIpService(SuspiciousIpRepository suspiciousIpRepository) {
        this.suspiciousIpRepository = suspiciousIpRepository;
    }

    public void validateIpFormat(String ip) throws SuspiciousIpInvalidFormatException{
        System.out.println(ip);
        if (ip == null) {
            throw new SuspiciousIpInvalidFormatException();
        }

        InetAddressValidator validator = InetAddressValidator.getInstance();
        if (!validator.isValid(ip)){
            throw new SuspiciousIpInvalidFormatException();
        }
    }

    public SuspiciousIp create(SuspiciousIpInDto dataIn) throws SuspiciousIpExistsException {
        Optional<SuspiciousIp> suspiciousIp = suspiciousIpRepository.findByIp(dataIn.ip());
        if (suspiciousIp.isPresent()) {
            throw new SuspiciousIpExistsException();
        }
        return suspiciousIpRepository.save(dataIn.toEntity());
    }

    public SuspiciousIp findByIp(String ip) throws SuspiciousIpDontExistsException {
        return suspiciousIpRepository.findByIp(ip).orElseThrow(SuspiciousIpDontExistsException::new);
    }

    public void delete(String ip) {
        SuspiciousIp suspiciousIp = findByIp(ip);
        suspiciousIpRepository.delete(suspiciousIp);
    }

    public List<SuspiciousIp> findAll(){
        return suspiciousIpRepository.findAllByOrderByIdAsc();
    }

    public boolean ipExists(String ip) {
        try {
            findByIp(ip);
        } catch(SuspiciousIpDontExistsException e) {
            return false;
        }
        return true;
    }
}
