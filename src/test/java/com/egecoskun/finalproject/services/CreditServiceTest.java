package com.egecoskun.finalproject.services;

import com.egecoskun.finalproject.model.Applicant;
import com.egecoskun.finalproject.model.Credit;
import com.egecoskun.finalproject.model.CreditRating;
import com.egecoskun.finalproject.repository.CreditRepository;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    CreditRepository creditRepository;

    @InjectMocks
    CreditService creditService;

    @Test
    void getAllCredits() {
        List<Credit> creditList = getSampleCredits();

        Mockito.when(creditRepository.findAll()).thenReturn(creditList);

        List<Credit> actualList=creditService.getAllCredits();

        Assert.assertEquals(actualList.size(),creditList.size());
    }

    @Test
    void getById() {
        Credit credit = getSampleCredits().get(1);
        Optional<Credit> optionalCredit = Optional.of(credit);

        Mockito.when(creditRepository.findById(Mockito.any())).thenReturn(optionalCredit);

        Credit realCredit = creditService.getById(1L);

        Assert.assertEquals(credit.getCreditResult(),realCredit.getCreditResult());
        Assert.assertEquals(credit.getCreditBalance(),realCredit.getCreditBalance());
    }

    @Test
    void create() {
        Credit expected = getSampleCredits().get(0);
        expected.setId(null);
        expected.setCreditBalance(0);

        Mockito.when(creditRepository.save(any())).thenReturn(expected);

        Credit credit = creditService.create();

        verify(creditRepository,times(1)).save(expected);

        Assert.assertEquals(credit.getCreditBalance(),expected.getCreditBalance());
    }

    @Test
    void delete() {
        Long creditId = 1L;
        Credit credit = getSampleCredits().get(0);


        Mockito.when(creditRepository.findById(creditId)).thenReturn(Optional.of(credit));
        doNothing().when(creditRepository).deleteById(creditId);

        //validate step
        creditService.delete(1L);

        verify(creditRepository, times(1)).deleteById(creditId);
    }

    private List<Credit> getSampleCredits() {
        List<Credit> sampleList = new ArrayList<>();
        Credit credit = new Credit(1L,1515,null);
        Credit credit1 = new Credit(2L,1400,"");
        Credit credit2 = new Credit(3L,50,"Credit Result : Approved");

        sampleList.add(credit);
        sampleList.add(credit1);
        sampleList.add(credit2);

        return sampleList;
    }
}