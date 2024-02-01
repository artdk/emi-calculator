package org.adk.emicalculator;

import jakarta.transaction.Transactional;
import org.adk.emicalculator.persistence.EmiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Transactional
public class CalculateEmi {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Autowired
    private EmiRepository emiRepository;

    public double calculate(int loanAmount, double yearlyInterestRate, int loanTermInYears, String email) {
        validateLoanAmount(loanAmount);
        validateYearlyInterestRate(yearlyInterestRate);
        validateLoanTerm(loanTermInYears);
        validateEmail(email);

        double emi = calculateEmi(loanAmount, yearlyInterestRate, loanTermInYears * 12);
        emiRepository.persistEmi(emi, email);
        return emi;
    }

    private double calculateEmi(int loanAmount, double yearlyInterestRate, int loanTermInMonths) {
        double monthlyInterestRate = yearlyInterestRate / 1200.0;
        double compoundRate = Math.pow(1 + monthlyInterestRate, loanTermInMonths);
        return loanAmount * monthlyInterestRate * compoundRate / (compoundRate - 1);
    }

    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) throw new EmailInvalid();
    }

    private void validateLoanAmount(int loanAmount) {
        if (loanAmount <= 0) throw new LoanAmountTooLow();
    }

    private void validateYearlyInterestRate(double yearlyInterestRate) {
        if (yearlyInterestRate <= 0.0 || yearlyInterestRate >= 100.0) throw new InterestRateInvalid();
    }

    private void validateLoanTerm(int loanTerm) {
        if (loanTerm <= 0 || loanTerm >= 30) throw new LoanTermInvalid();
    }

    static class LoanAmountTooLow extends ValidationException {
        LoanAmountTooLow() {
            super("Loan amounts must be greater than 0");
        }
    }

    static class InterestRateInvalid extends ValidationException {
        InterestRateInvalid() {
            super("Interest rates must be between 0 and 100");
        }
    }

    static class LoanTermInvalid extends ValidationException {
        LoanTermInvalid() {
            super("Loan term must be between 0 and 30");
        }
    }

    static class EmailInvalid extends ValidationException {
        EmailInvalid() {
            super("Email addresses should match " + EMAIL_REGEX);
        }
    }
}
