package com.egecoskun.finalproject.controller;

import com.egecoskun.finalproject.model.Applicant;
import com.egecoskun.finalproject.model.Credit;
import com.egecoskun.finalproject.model.DTO.ApplicantDTO;
import com.egecoskun.finalproject.services.ApplicantService;
import com.egecoskun.finalproject.services.CreditRatingService;
import com.egecoskun.finalproject.services.CreditService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applicant")
public class ApplicantController {

    private static final Gson gson = new Gson();

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
    public ResponseEntity createNewJobSeeker(@RequestBody ApplicantDTO applicantDTO) {
        Applicant applicant = applicantService.create(applicantDTO);

        if (applicant == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Job seeker could not be created successfully");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(applicant);
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteApplicant(@RequestParam(name = "id") Long id) {
        applicantService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Related applicant deleted successfully");
    }


    @PutMapping("/apply")
    public ResponseEntity applyToCredit(@RequestParam(name = "id") Long applicantId, @RequestParam(name = "jobId") Long creditId) {

        Applicant applicant = applicantService.getById(applicantId);
        Credit credit = creditService.getById(creditId);

        applicant.setCredit(credit);


        if (applicant.getCredit() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not apply to credit");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Applied to credit!");
        }
    }

    @GetMapping("/creditresult/{id}")
    public ResponseEntity getCreditResultById(@PathVariable("id") Long applicantId) {
        Applicant byId = applicantService.getById(applicantId);
       String creditResult= byId.getCredit().getCreditResult();
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(creditResult));
    }
}
