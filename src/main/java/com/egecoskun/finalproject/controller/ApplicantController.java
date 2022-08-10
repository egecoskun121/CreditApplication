package com.egecoskun.finalproject.controller;

import com.egecoskun.finalproject.model.Applicant;
import com.egecoskun.finalproject.model.DTO.ApplicantDTO;
import com.egecoskun.finalproject.services.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/applicant")
public class ApplicantController {


    private ApplicantService applicantService;


    @Autowired
    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity getAllApplicants() {
        List<Applicant> allApplicants = applicantService.getAllApplicants();

        return ResponseEntity.ok(allApplicants);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity getApplicantById(@PathVariable("id") Long id) {
        Applicant byId = applicantService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(byId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @GetMapping("/byIn/{in}")
    public ResponseEntity getApplicantByIdentificationNumber(@PathVariable("in") Long identificationNumber) {
        Optional<Applicant> byIn = applicantService.getByIdentificationNumber(identificationNumber);
        return ResponseEntity.status(HttpStatus.OK).body(byIn);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @PostMapping("/create")
    public ResponseEntity createNewApplicant(@RequestBody ApplicantDTO applicantDTO) {
        Applicant applicant = applicantService.create(applicantDTO);

        if (applicant == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Applicant could not be created successfully");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(applicant);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteApplicant(@PathVariable(name = "id") Long id) {
        applicantService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Related applicant deleted successfully");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity updateApplicant(@RequestBody ApplicantDTO applicantDTO,@PathVariable(name = "id") Long id) {
        applicantService.update(applicantDTO,id);
        return ResponseEntity.status(HttpStatus.OK).body("Related applicant updated successfully");
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @PutMapping("/apply/{id}")
    public ResponseEntity applyToCredit(@PathVariable(name = "id") Long applicantId) {

        applicantService.applyToCredit(applicantId);
        Applicant applicant=applicantService.getById(applicantId);


         if (Objects.equals(applicant.getCredit().getCreditResult(), "Kredi Sonucu : Onay")) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("""
                            Applied to credit succesfully.
                            """+applicant.getCredit().getCreditResult());
        } else if(Objects.equals(applicant.getCredit().getCreditResult(), "Kredi Sonucu : Red")) {
             return ResponseEntity.status(HttpStatus.OK)
                     .body("""
                            Applied to credit succesfully.
                            """+applicant.getCredit().getCreditResult());
        }else{
             return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Could not applied to credit!");
         }
    }




}
