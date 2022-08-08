package com.egecoskun.finalproject.controller;


import com.egecoskun.finalproject.model.Applicant;
import com.egecoskun.finalproject.model.Credit;
import com.egecoskun.finalproject.model.DTO.CreditDTO;
import com.egecoskun.finalproject.model.mapper.CreditMapper;
import com.egecoskun.finalproject.repository.ApplicantRepository;
import com.egecoskun.finalproject.services.ApplicantService;
import com.egecoskun.finalproject.services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/credit")
public class CreditController {

    private CreditService creditService;
    private ApplicantService applicantService;
    private CreditMapper creditMapper;
    private ApplicantRepository applicantRepository;

    @Autowired
    public CreditController(CreditService creditService, ApplicantService applicantService, CreditMapper creditMapper, ApplicantRepository applicantRepository) {
        this.creditService = creditService;
        this.applicantService = applicantService;
        this.creditMapper = creditMapper;
        this.applicantRepository = applicantRepository;
    }


    @GetMapping("/all")
    public ResponseEntity getAllCredits(){
        List<Credit> allCredits = creditService.getAllCredits();

        return ResponseEntity.ok(allCredits);
    }

    @GetMapping("/id")
    public ResponseEntity getCreditById(@PathVariable("id") Long id){
        Credit credit = creditService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(credit);
    }

    @GetMapping("/{in}")
    public ResponseEntity getCreditByIdentificationNumber(@PathVariable("in") Long identificationNumber){
        Credit byId = creditService.getById(identificationNumber);

        return ResponseEntity.status(HttpStatus.OK).body(byId);
    }

    @PostMapping("/create/{id}")
    public ResponseEntity createCredit(@RequestBody CreditDTO creditDTO,@PathVariable("id") Long id){
        Applicant applicant = applicantService.getById(id);
        Credit credit = creditMapper.toEntity(creditDTO);
        applicant.setCredit(credit);
        applicantRepository.save(applicant);

        if(applicant.getCredit()==null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Credit could not be created successfully");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(credit);
    }

    @DeleteMapping
    public ResponseEntity deleteCredit(@RequestParam(name = "id") Long id) {
        creditService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body("Related credit deleted successfully");
    }
}
