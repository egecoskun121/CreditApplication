package com.egecoskun.finalproject.services;


import com.egecoskun.finalproject.exception.ApplicantNotFoundException;
import com.egecoskun.finalproject.exception.EntityNotFoundException;
import com.egecoskun.finalproject.model.Applicant;
import com.egecoskun.finalproject.model.Credit;
import com.egecoskun.finalproject.model.DTO.ApplicantDTO;
import com.egecoskun.finalproject.model.mapper.ApplicantMapper;
import com.egecoskun.finalproject.repository.ApplicantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

@Slf4j
@Service
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final CreditService creditService;

    @Autowired
    public ApplicantService(ApplicantRepository applicantRepository, CreditService creditService) {
        this.applicantRepository = applicantRepository;
        this.creditService = creditService;
    }



    public List<Applicant> getAllApplicants() {
        List<Applicant> allApplicants = applicantRepository.findAll();
        return allApplicants;
    }

    public Applicant getById(Long id) {
        Optional<Applicant> byId = applicantRepository.findById(id);
        return byId.orElseThrow(() -> {
            log.error("Course not found by id : " + id);
            return new EntityNotFoundException("Course", "id : " + id);
        });
    }

    public Applicant create(ApplicantDTO applicantDTO) {
        Applicant applicant = ApplicantMapper.toEntity(applicantDTO);
        return applicantRepository.save(applicant);
    }

    public void delete(Long id) {
        getById(id);
        applicantRepository.deleteById(id);
    }

    public Optional<Applicant> getByIdentificationNumber(Long id){
        Optional<Applicant> byIdentificationNumber = applicantRepository.getApplicantByIdentificationNumber(id);
        return Optional.ofNullable(byIdentificationNumber.orElseThrow(() -> {
            log.error("Applicant not found by identification number : " + id);
            return new ApplicantNotFoundException("Applicant", id);
        }));
    }

    public ResponseEntity applyToCredit(@RequestParam(name = "id") Long applicantId) {

        Applicant applicant = getById(applicantId);
        Credit credit = applicant.getCredit();

        int creditRatingEvaluation=creditService.gradeMap.floorEntry(650).getValue();




        if(creditService.gradeMap.floorEntry(650).getValue()==0);



            if (applicant.getCredit() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Could not apply to credit");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Applied to credit!");
            }
    }


}
