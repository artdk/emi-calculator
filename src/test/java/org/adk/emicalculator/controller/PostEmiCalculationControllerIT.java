package org.adk.emicalculator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostEmiCalculationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetEmiCalculation() throws Exception {
        String body = """
                {
                    "email": "j.doe@gmail.com",
                    "loan_amount": 15000,
                    "interest_rate": 6.8,
                    "loan_term": 18
                }
                """.stripIndent();

        mockMvc.perform(post("/emi").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emi").value(878.9125572261006));
    }

    @Test
    void shouldReturnProblemDetailForInvalidInput() throws Exception {
        String body = """
                {
                    "email": "j.doe@gmail.com",
                    "loan_amount": -10,
                    "interest_rate": 6.8,
                    "loan_term": 18
                }
                """.stripIndent();

        mockMvc.perform(post("/emi").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("LoanAmountTooLow"))
                .andExpect(jsonPath("$.detail").value("Loan amounts must be greater than 0"))
                .andExpect(jsonPath("$.instance").value("/emi"));
    }
}
