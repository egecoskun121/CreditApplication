package com.egecoskun.finalproject.controller;

import com.egecoskun.finalproject.exception.handler.GenericExceptionHandler;
import com.egecoskun.finalproject.model.Applicant;
import com.egecoskun.finalproject.model.Credit;
import com.egecoskun.finalproject.model.DTO.ApplicantDTO;
import com.egecoskun.finalproject.services.ApplicantService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ApplicantControllerTest {


    private MockMvc mvc;

    @Mock
    private ApplicantService applicantService;

    @InjectMocks
    private ApplicantController applicantController;

    @BeforeEach
    public void setup() {

        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(applicantController)
                .setControllerAdvice(new GenericExceptionHandler())
                .build();
    }





    @Test
    void getAllApplicants() throws Exception {

        List<Applicant> expectedApplicants = getTestApplicants();

        when(applicantService.getAllApplicants()).thenReturn(expectedApplicants);

        MockHttpServletResponse response = mvc.perform(get("/api/v1/applicant/all")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        List<Applicant> actualApplicantList = new ObjectMapper().readValue(response.getContentAsString(), new TypeReference<List<Applicant>>() {
        });

        assertEquals(expectedApplicants.size(),actualApplicantList.size());
    }

    @Test
    void getApplicantById() throws Exception {

        List<Applicant> expectedApplicants = getTestApplicants();

        // stub - given
        when(applicantService.getById(1L)).thenReturn(expectedApplicants.get(0));

        MockHttpServletResponse response = mvc.perform(get("/api/v1/applicant/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Applicant applicant = new ObjectMapper().readValue(response.getContentAsString(), Applicant.class);
        Assert.assertEquals(expectedApplicants.get(0).getIdentificationNumber(), applicant.getIdentificationNumber());
    }

    @Test
    void getApplicantByIdentificationNumber() throws Exception {

        List<Applicant> expectedApplicants = getTestApplicants();

        // stub - given
        when(applicantService.getByIdentificationNumber(1L)).thenReturn(Optional.ofNullable(expectedApplicants.get(0)));

        MockHttpServletResponse response = mvc.perform(get("/api/v1/applicant/byIn/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Applicant applicant = new ObjectMapper().readValue(response.getContentAsString(), Applicant.class);
        Assert.assertEquals(expectedApplicants.get(0).getIdentificationNumber(), applicant.getIdentificationNumber());
    }

    @Test
    void createNewApplicant() {

    }

    @Test
    void deleteApplicant() throws Exception {

    }

    @Test
    void updateApplicant() throws Exception {
        Applicant applicant = getTestApplicants().get(0);
        ApplicantDTO applicantDTO = new ApplicantDTO(12315133L,"Ege","Coskn",5342,"3510234");
        Applicant updated = new Applicant(applicant.getId(),applicantDTO.getIdentificationNumber(),applicantDTO.getFirstName(),applicantDTO.getLastName()
                ,applicantDTO.getMonthlyIncome(),applicantDTO.getPhoneNumber(),null,null);
        String message ="Related applicant updated successfully";


        when(applicantService.update(applicantDTO,1L)).thenReturn(updated);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String expectedApplicantJsonStr = ow.writeValueAsString(applicant);


        MockHttpServletResponse response = mvc.perform(put("/api/v1/applicant/update/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedApplicantJsonStr))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String succesful = response.getContentAsString();

        Assert.assertEquals(succesful,message);
    }

    @Test
    void applyToCredit_successful() throws Exception {
        Applicant applicant = getTestApplicants().get(0);
        Credit credit = new Credit(1L,10000,"Credit Result : Approved");
        applicant.setCredit(credit);


        when(applicantService.getById(1l)).thenReturn(applicant);
        when(applicantService.applyToCredit(1L)).thenReturn(applicant);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String expectedApplicantJsonStr = ow.writeValueAsString(applicant);


        MockHttpServletResponse response = mvc.perform(put("/api/v1/applicant/apply/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedApplicantJsonStr))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    void applyToCredit_fail() throws Exception {
        Applicant applicant = getTestApplicants().get(0);
        Credit credit = new Credit(1L,0,"Credit Result : Declined");
        applicant.setCredit(credit);


        when(applicantService.getById(1l)).thenReturn(applicant);
        when(applicantService.applyToCredit(1L)).thenReturn(applicant);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String expectedApplicantJsonStr = ow.writeValueAsString(applicant);


        MockHttpServletResponse response = mvc.perform(put("/api/v1/applicant/apply/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedApplicantJsonStr))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());


    }

    private List<Applicant> getTestApplicants() {
        // init test values
        List<Applicant> applicantList = new ArrayList<>();
        Applicant applicant1 = new Applicant(1L,12415123L,"Kek","Wait",4200,"055324005",null,null);
        Applicant applicant2 = new Applicant(2L,12415123L,"Basar","cos",5125,"055353405",null,null);
        Applicant applicant3 = new Applicant(3L,12415123L,"Omega","Lul",7823,"055325005",null,null);

        applicantList.add(applicant1);
        applicantList.add(applicant2);
        applicantList.add(applicant3);

        return applicantList;
    }

}