package antifraud.service;

import antifraud.dto.StolenCardCreateInDto;
import antifraud.exceptions.CardNumberIsInvalidException;
import antifraud.exceptions.StolenCardExistsException;
import antifraud.exceptions.StolenCardNotFoundException;
import antifraud.model.StolenCard;
import antifraud.repository.StolenCardRepository;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StolenCardService {

    final StolenCardRepository stolenCardRepository;

    @Autowired
    public StolenCardService(StolenCardRepository stolenCardRepository) {
        this.stolenCardRepository = stolenCardRepository;
    }

    public void validateCardNumber(String number) throws CardNumberIsInvalidException {
        if (!LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(number)) {
            throw new CardNumberIsInvalidException();
        }
    }

    public StolenCard create(StolenCardCreateInDto dataIn) throws StolenCardExistsException {
        Optional<StolenCard> stolenCard = stolenCardRepository.findByNumber(dataIn.number());
        if (stolenCard.isPresent()) {
            throw new StolenCardExistsException();
        }
        return stolenCardRepository.save(dataIn.toEntity());
    }

    public StolenCard findByNumber(String number) throws StolenCardNotFoundException {
        return stolenCardRepository.findByNumber(number).orElseThrow(StolenCardNotFoundException::new);
    }

    public void delete(String number) {
        stolenCardRepository.delete(findByNumber(number));
    }

    public List<StolenCard> findAll() {
        return stolenCardRepository.findAllByOrderByIdAsc();
    }

    public boolean numberExists(String number) {
        try {
            findByNumber(number);
        } catch(StolenCardNotFoundException e) {
            return false;
        }
        return true;
    }
}
