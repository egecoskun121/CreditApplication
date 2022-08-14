package com.egecoskun.finalproject.services;

import com.egecoskun.finalproject.exception.ApplicantNotFoundException;
import com.egecoskun.finalproject.exception.EntityNotFoundException;
import com.egecoskun.finalproject.model.Applicant;
import com.egecoskun.finalproject.model.Credit;
import com.egecoskun.finalproject.model.DTO.ApplicantDTO;
import com.egecoskun.finalproject.model.mapper.ApplicantMapper;
import com.egecoskun.finalproject.repository.ApplicantRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ApplicantServiceTest {


    @Mock
    private ApplicantRepository applicantRepository;
    @Mock
    private CreditRatingService creditRatingService;
    @Mock
    private ApplicantMapper applicantMapper;
    @Mock
    private CreditService creditService;

    @InjectMocks
    private ApplicantService applicantService;



    @Test
    void getAllApplicants() {

        List<Applicant> applicantList = getSampleApplicants();

        Mockito.when(applicantRepository.findAll()).thenReturn(applicantList);

        List<Applicant> actualApplicantList=applicantService.getAllApplicants();

        Assert.assertEquals(actualApplicantList.size(),applicantList.size());

        actualApplicantList=actualApplicantList.stream().sorted(getApplicantComparator()).collect(Collectors.toList());
        applicantList=applicantList.stream().sorted(getApplicantComparator()).collect(Collectors.toList());

        for(int i=0;i<actualApplicantList.size();i++){
            Applicant a1 = applicantList.get(i);
            Applicant a2 = actualApplicantList.get(i);

            assertEquals(a1.getMonthlyIncome(), a2.getMonthlyIncome());
            Assert.assertEquals(a1.getFirstName(),a2.getFirstName());
            Assert.assertEquals(a1.getLastName(),a2.getLastName());
            Assert.assertEquals(a1.getCredit(),a2.getCredit());
            Assert.assertEquals(a1.getCreditRating(),a2.getCreditRating());
            Assert.assertEquals(a1.getIdentificationNumber(),a2.getIdentificationNumber());
            Assert.assertEquals(a1.getPhoneNumber(),a2.getPhoneNumber());
        }
    }

    @Test
    void getById() {

        Applicant applicant = getSampleApplicants().get(1);
        Optional<Applicant> optionalApplicant = Optional.of(applicant);

        Mockito.when(applicantRepository.findById(Mockito.any())).thenReturn(optionalApplicant);

        Applicant realApplicant = applicantService.getById(1L);

        Assert.assertEquals(applicant.getFirstName(),realApplicant.getFirstName());
        Assert.assertEquals(applicant.getLastName(),realApplicant.getLastName());
        Assert.assertEquals(applicant.getCredit(),realApplicant.getCredit());
        Assert.assertEquals(applicant.getCreditRating(),realApplicant.getCreditRating());
        Assert.assertEquals(applicant.getIdentificationNumber(),realApplicant.getIdentificationNumber());
        Assert.assertEquals(applicant.getPhoneNumber(),realApplicant.getPhoneNumber());
    }

    @Test
    void getById_NOT_FOUND() {
        Mockito.when(applicantRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> {
                    Applicant applicant = applicantService.getById(any());
                }
        );
    }

    @Test
    void create() {
        Applicant expected = getSampleApplicants().get(0);
        expected.setId(null);

        Mockito.when(applicantRepository.save(any())).thenReturn(expected);

        ApplicantDTO applicantDTO = new ApplicantDTO();
        applicantDTO.setFirstName(expected.getFirstName());
        applicantDTO.setIdentificationNumber(expected.getIdentificationNumber());
        applicantDTO.setLastName(expected.getLastName());
        applicantDTO.setMonthlyIncome(expected.getMonthlyIncome());
        applicantDTO.setPhoneNumber(expected.getPhoneNumber());

        Applicant actualApplicant = applicantService.create(applicantDTO);

        verify(applicantRepository, times(1)).save(expected);

        Assert.assertEquals(expected.getPhoneNumber(), actualApplicant.getPhoneNumber());
        Assert.assertEquals(expected.getFirstName(), actualApplicant.getFirstName());
        Assert.assertEquals(expected.getIdentificationNumber(), actualApplicant.getIdentificationNumber());
    }

    @Test
    void delete() {
        Long applicantId = 1L;
        Applicant applicant = getSampleApplicants().get(0);


        Mockito.when(applicantRepository.findById(applicantId)).thenReturn(Optional.of(applicant));
        doNothing().when(applicantRepository).deleteById(applicantId);

        //validate step
        applicantService.delete(1L);

        verify(applicantRepository, times(1)).deleteById(applicantId);
    }

    @Test
    void update() {
        Applicant applicant = getSampleApplicants().get(0);
        Applicant updatedApplicant = new Applicant(1L,2141245L,"ege","c0skun",2523,"21412412",null,null);

        Mockito.when(applicantRepository.findById(anyLong())).thenReturn(Optional.ofNullable(applicant));
        Mockito.when(applicantRepository.save(notNull())).thenAnswer(returnsFirstArg());

        applicantService.update(applicantMapper.toDTO(updatedApplicant),anyLong());

        Assert.assertEquals(applicant.getIdentificationNumber(),updatedApplicant.getIdentificationNumber());
        Assert.assertEquals(applicant.getPhoneNumber(), updatedApplicant.getPhoneNumber());
        Assert.assertEquals(applicant.getFirstName(), updatedApplicant.getFirstName());
        assertEquals(applicant.getMonthlyIncome(), updatedApplicant.getMonthlyIncome());
        Assert.assertEquals(applicant.getId(), updatedApplicant.getId());
    }

    @Test
    void getByIdentificationNumber() {
        Applicant applicant = getSampleApplicants().get(1);
        Optional<Applicant> optionalApplicant = Optional.of(applicant);

        Mockito.when(applicantRepository.getApplicantByIdentificationNumber(any())).thenReturn(applicant.getId());
        Mockito.when(applicantRepository.findById(Mockito.any())).thenReturn(optionalApplicant);

        Optional<Applicant> realApplicant = applicantService.getByIdentificationNumber(1L);

        // using Lombok's equals method
        assertEquals(Optional.of(applicant), realApplicant);
    }

    @Test
    void getByIdentificationNumber_NOT_FOUND() {

        Long id=1L;
        Mockito.when(applicantRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> {
                    Applicant applicant = applicantService.getById(any());
                }
        );
    }

    @Test
    void applyToCredit(){
        Long applicantId=1L;
        Credit credit = new Credit(1L,0,"Kredi sonucu : Onay");
        Applicant applicant = getSampleApplicants().get(0);

        Mockito.when(applicantRepository.findById(applicantId)).thenReturn(Optional.ofNullable(applicant));
        Mockito.when(creditService.create()).thenReturn(credit);


        applicantService.applyToCredit(applicantId);

    }


    private List<Applicant> getSampleApplicants() {
        List<Applicant> sampleList = new ArrayList<>();
        Applicant applicant1 = new Applicant(1L,12415123L,"Kek","Wait",4200,"055324005",null,null);
        Applicant applicant2 = new Applicant(2L,12415123L,"Basar","cos",5125,"055353405",null,null);
        Applicant applicant3 = new Applicant(2L,12415123L,"Omega","Lul",7823,"055325005",null,null);
        sampleList.add(applicant1);
        sampleList.add(applicant2);
        sampleList.add(applicant3);
        return sampleList;
    }

    private Comparator<Applicant> getApplicantComparator() {
        return (o1, o2) -> {
            if (o1.getId() - o2.getId() < 0)
                return -1;
            if (o1.getId() - o2.getId() == 0)
                return 0;
            return 1;
        };
    }
}