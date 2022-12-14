package com.egecoskun.finalproject.services;


import com.egecoskun.finalproject.exception.ApplicantNotFoundException;
import com.egecoskun.finalproject.exception.CreditException;
import com.egecoskun.finalproject.exception.EntityNotFoundException;
import com.egecoskun.finalproject.model.Applicant;
import com.egecoskun.finalproject.model.Credit;
import com.egecoskun.finalproject.model.DTO.ApplicantDTO;
import com.egecoskun.finalproject.model.mapper.ApplicantMapper;
import com.egecoskun.finalproject.repository.ApplicantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final CreditService creditService;
    private final CreditRatingService creditRatingService;
    private final int CREDIT_MULTIPLIER=4;

    @Autowired
    public ApplicantService(ApplicantRepository applicantRepository, CreditService creditService, CreditRatingService creditRatingService) {
        this.applicantRepository = applicantRepository;
        this.creditService = creditService;
        this.creditRatingService = creditRatingService;
    }


    public List<Applicant> getAllApplicants() {
        List<Applicant> allApplicants = applicantRepository.findAll();
        return allApplicants;
    }

    public Applicant getById(Long id) {
        Optional<Applicant> byId = applicantRepository.findById(id);
        return byId.orElseThrow(() -> {
            log.error("Applicant not found by id : " + id);
            return new EntityNotFoundException("Applicant", "id : " + id);
        });
    }

    public Applicant create(ApplicantDTO applicantDTO) {
        Applicant applicant = ApplicantMapper.toEntity(applicantDTO);
        applicant.setCreditRating(creditRatingService.create());
        return applicantRepository.save(applicant);
    }

    public void delete(Long id) {
        getById(id);
        applicantRepository.deleteById(id);
    }

    public Applicant addCreditToApplicant(Credit credit, Long applicantId) {
        Applicant byId = getById(applicantId);
        if (getById(applicantId)==null)
            throw new ApplicantNotFoundException("Applicant",applicantId);

           log.debug("Applicant credit : " + byId.getCredit());
            byId.setCredit(credit);

        return applicantRepository.save(byId);
    }

    public Applicant update(ApplicantDTO applicantDTO,Long id){
            Applicant byId = getById(id);

            byId.setMonthlyIncome(applicantDTO.getMonthlyIncome());
            byId.setFirstName(applicantDTO.getFirstName());
            byId.setLastName(applicantDTO.getLastName());
            byId.setPhoneNumber(applicantDTO.getPhoneNumber());
            byId.setIdentificationNumber(applicantDTO.getIdentificationNumber());

        return applicantRepository.save(byId);
    }

    public Optional<Applicant> getByIdentificationNumber(Long identificationNumber){

        Long id = applicantRepository.getApplicantByIdentificationNumber(identificationNumber);
        Optional<Applicant> byIdentificationNumber=applicantRepository.findById(id);
        return Optional.ofNullable(byIdentificationNumber.orElseThrow(() -> {
            log.error("Applicant not found by identification number : " + id);
            return new ApplicantNotFoundException("Applicant", id);
        }));
    }

    public Applicant applyToCredit(@RequestParam(name = "id") Long applicantId) {
        Applicant applicant = getById(applicantId);

        if(applicant.getCredit()==null){
            Credit credit = creditService.create();
            int creditRatingEvaluation=creditService.gradeMap.floorEntry(applicant.getCreditRating().getCreditRating()).getValue();

            if(creditRatingEvaluation==0){ //0-499
                credit.setCreditResult("Credit Result : Declined");
                credit.setCreditBalance(0);
            }else if(creditRatingEvaluation==1 && applicant.getMonthlyIncome()<=5000){ // 500-999
                credit.setCreditResult("Credit Result : Approved");
                credit.setCreditBalance(10000);
            }else if(creditRatingEvaluation==1 && applicant.getMonthlyIncome()>5000){ // 500-999
                credit.setCreditResult("Credit Result : Approved");
                credit.setCreditBalance(20000);
            }else if(creditRatingEvaluation>1){ // 1000-+
                credit.setCreditResult("Credit Result : Approved");
                credit.setCreditBalance((int) (applicant.getMonthlyIncome()*CREDIT_MULTIPLIER));
            }

            // imaginary sms sent to customer here !
            creditService.sendSMS(applicant.getPhoneNumber());

            return addCreditToApplicant(credit,applicantId);
        }else{
            log.error("Can only apply once to a credit");
            throw new CreditException("You already applied to a credit!");
        }
    }
}
