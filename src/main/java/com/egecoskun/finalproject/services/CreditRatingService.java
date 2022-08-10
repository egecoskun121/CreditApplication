package com.egecoskun.finalproject.services;

import com.egecoskun.finalproject.exception.EntityNotFoundException;
import com.egecoskun.finalproject.model.Credit;
import com.egecoskun.finalproject.model.CreditRating;
import com.egecoskun.finalproject.model.DTO.CreditRatingDTO;
import com.egecoskun.finalproject.model.mapper.CreditRatingMapper;
import com.egecoskun.finalproject.repository.CreditRatingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class CreditRatingService {


    private final int LOWER_BOUND=0;
    private final int UPPER_BOUND=1900;
    private final CreditRatingRepository creditRatingRepository;

    @Autowired
    public CreditRatingService(CreditRatingRepository creditRatingRepository) {
        this.creditRatingRepository = creditRatingRepository;

    }

    public CreditRating getById(Long id) {
        Optional<CreditRating> byId = creditRatingRepository.findById(id);
        return byId.orElseThrow(() -> {
            log.error("Credit rating not found by id : " + id);
            return new EntityNotFoundException("Credit rating ", "id : " + id);
        });
    }



    public int getRandomCreditRating(){
        Random random = new Random();
        int randomCreditRating = random.nextInt(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
        return randomCreditRating;
    }

    public CreditRating create() {
        CreditRating creditRating = new CreditRating();
        creditRating.setCreditRating(getRandomCreditRating());
        return creditRatingRepository.save(creditRating);
    }

    public void delete(Long id) {
        getById(id);
        creditRatingRepository.deleteById(id);
    }
}
