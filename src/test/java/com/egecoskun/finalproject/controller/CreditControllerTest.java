package com.egecoskun.finalproject.controller;

import com.egecoskun.finalproject.exception.handler.GenericExceptionHandler;
import com.egecoskun.finalproject.model.Applicant;
import com.egecoskun.finalproject.model.Credit;
import com.egecoskun.finalproject.services.CreditService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CreditControllerTest {

    private MockMvc mvc;

    @Mock
    private CreditService creditService;

    @InjectMocks
    private CreditController creditController;

    @BeforeEach
    public void setup() {

        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(creditController)
                .setControllerAdvice(new GenericExceptionHandler())
                .build();
    }

    @Test
    void getAllCredits() throws Exception {
        List<Credit> expectedCredits = getTestCredits();

        when(creditService.getAllCredits()).thenReturn(expectedCredits);

        MockHttpServletResponse response = mvc.perform(get("/api/v1/credit/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        List<Credit> actualCreditList = new ObjectMapper().readValue(response.getContentAsString(), new TypeReference<List<Credit>>() {
        });

        assertEquals(expectedCredits.size(),actualCreditList.size());
    }

    @Test
    void getCreditById() throws Exception {

        List<Credit> expectedCredits = getTestCredits();

        // stub - given
        when(creditService.getById(1L)).thenReturn(expectedCredits.get(0));

        MockHttpServletResponse response = mvc.perform(get("/api/v1/credit/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Credit credit = new ObjectMapper().readValue(response.getContentAsString(), Credit.class);
        Assert.assertEquals(expectedCredits.get(0).getCreditBalance(), credit.getCreditBalance());
    }


    public List<Credit> getTestCredits(){

        List<Credit> creditList = new ArrayList<>();
        Credit credit = new Credit(1L,5000,null);
        Credit credit1 = new Credit(2L,5000,"Credit Result : Approved");
        Credit credit2 = new Credit(3L,5000,"Credit Result : Declined");

        creditList.add(credit2);
        creditList.add(credit1);
        creditList.add(credit);


        return creditList;
    }
}