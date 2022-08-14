package com.egecoskun.finalproject.services;

import com.egecoskun.finalproject.model.Applicant;
import com.egecoskun.finalproject.model.CreditRating;
import com.egecoskun.finalproject.repository.ApplicantRepository;
import com.egecoskun.finalproject.repository.CreditRatingRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreditRatingServiceTest {

    @Mock
    private CreditRatingRepository creditRatingRepository;

    @InjectMocks
    private CreditRatingService creditRatingService;

    @Test
    void getById() {
        CreditRating creditRating = getSampleCreditRatings().get(1);
        Optional<CreditRating> optionalApplicant = Optional.of(creditRating);

        Mockito.when(creditRatingRepository.findById(Mockito.any())).thenReturn(optionalApplicant);

        CreditRating realCreditRating = creditRatingService.getById(1L);

        Assert.assertEquals(creditRating.getCreditRating(),realCreditRating.getCreditRating());
        Assert.assertEquals(creditRating.getId(),realCreditRating.getId());

    }



    private List<CreditRating> getSampleCreditRatings() {
        List<CreditRating> sampleList = new ArrayList<>();
        CreditRating creditRating1 = new CreditRating(1L,1515);
        CreditRating creditRating2 = new CreditRating(2L,1400);
        CreditRating creditRating3 = new CreditRating(3L,50);

        sampleList.add(creditRating1);
        sampleList.add(creditRating2);
        sampleList.add(creditRating3);

        return sampleList;
    }
}