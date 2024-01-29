package org.adk.emicalculator.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.adk.emicalculator.CalculateEmi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostEmiCalculationController {

    @Autowired
    private CalculateEmi calculateEmi;

    @PostMapping("/emi")
    Response post(@RequestBody Request request) {
        double emi = calculateEmi.calculate(request.loanAmount, request.interestRate, request.loanTerm, request.email);
        return new Response(emi);
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Request(String email, int loanAmount, double interestRate, int loanTerm) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Response(double emi) {}
}
