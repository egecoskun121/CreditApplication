package com.egecoskun.finalproject.services;

import com.egecoskun.finalproject.exception.EntityNotFoundException;
import com.egecoskun.finalproject.model.Applicant;
import com.egecoskun.finalproject.model.Credit;
import com.egecoskun.finalproject.model.DTO.ApplicantDTO;
import com.egecoskun.finalproject.model.DTO.CreditDTO;
import com.egecoskun.finalproject.model.mapper.ApplicantMapper;
import com.egecoskun.finalproject.model.mapper.CreditMapper;
import com.egecoskun.finalproject.repository.CreditRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CreditService {

    private final CreditRepository creditRepository;

    @Autowired
    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public List<Credit> getAllCredits() {
        List<Credit> allCredits = creditRepository.findAll();
        return allCredits;
    }

    public Credit getById(Long id) {
        Optional<Credit> byId = creditRepository.findById(id);
        return byId.orElseThrow(() -> {
            log.error("Credit not found by id : " + id);
            return new EntityNotFoundException("Credit", "id : " + id);
        });
    }

    public Credit create(CreditDTO creditDTO) {
        Credit credit = CreditMapper.toEntity(creditDTO);
        return creditRepository.save(credit);
    }

    public void delete(Long id) {
        getById(id);
        creditRepository.deleteById(id);
    }
}
