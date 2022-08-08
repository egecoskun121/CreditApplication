package com.egecoskun.finalproject.controller;

import com.egecoskun.finalproject.model.Applicant;
import com.egecoskun.finalproject.model.Credit;
import com.egecoskun.finalproject.model.DTO.ApplicantDTO;
import com.egecoskun.finalproject.services.ApplicantService;
import com.egecoskun.finalproject.services.CreditRatingService;
import com.egecoskun.finalproject.services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applicant")
public class ApplicantController {


    private ApplicantService applicantService;
    private CreditService creditService;
    private CreditRatingService creditRatingService;

    @Autowired
    public ApplicantController(ApplicantService applicantService, CreditService creditService, CreditRatingService creditRatingService) {
        this.applicantService = applicantService;
        this.creditService = creditService;
        this.creditRatingService = creditRatingService;
    }

    @GetMapping("/all")
    public ResponseEntity getAllApplicants() {
        List<Applicant> allJobSeekers = applicantService.getAllApplicants();

        return ResponseEntity.ok(allJobSeekers);
    }

    @GetMapping("/{id}")
    public ResponseEntity getApplicantById(@PathVariable("id") Long id) {
        Applicant byId = applicantService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(byId);
    }

    @PostMapping("/create")
    public ResponseEntity createNewApplicant(@RequestBody ApplicantDTO applicantDTO) {
        Applicant applicant = applicantService.create(applicantDTO);

        if (applicant == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Applicant could not be created successfully");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(applicant);
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteApplicant(@RequestParam(name = "id") Long id) {
        applicantService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Related applicant deleted successfully");
    }


    @PutMapping("/apply")
    public ResponseEntity applyToCredit(@RequestParam(name = "id") Long applicantId) {

        applicantService.applyToCredit(applicantId);

        /*if (applicant.getCredit() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not apply to credit");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Applied to credit!");
        }*/
    }


}
