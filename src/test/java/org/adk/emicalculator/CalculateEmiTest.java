package org.adk.emicalculator;

import org.adk.emicalculator.persistence.EmiRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CalculateEmiTest {
    private static final String VALID_EMAIL = "j.doe@gmail.com";

    @Mock
    private EmiRepository emiRepository;

    @InjectMocks
    private CalculateEmi sut;

    @ParameterizedTest
    @ValueSource(strings = {"email.com", "quotes'this'at@that.com", "j.doe@this@that.com"})
    void shouldThrowIfEmailInvalid(String email) {
        assertThrows(
                CalculateEmi.EmailInvalid.class,
                () -> sut.calculate(1, 1.0, 1, email)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void shouldThrowIfLoanAmountInvalid(int loanAmount) {
        assertThrows(
                CalculateEmi.LoanAmountTooLow.class,
                () -> sut.calculate(loanAmount, 1.0, 1, VALID_EMAIL)
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {-9.9, 0.0, 100.0, 314.15})
    void shouldThrowIfInterestRateInvalid(double yearlyInterestRate) {
        assertThrows(
                CalculateEmi.InterestRateInvalid.class,
                () -> sut.calculate(1, yearlyInterestRate, 1, VALID_EMAIL)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 30, 55})
    void shouldThrowIfLoanTermInvalid(int loanTerm) {
        assertThrows(
                CalculateEmi.LoanTermInvalid.class,
                () -> sut.calculate(1, 1.0, loanTerm, VALID_EMAIL)
        );
    }

    @ParameterizedTest
    @MethodSource("validEmiCalculations")
    void shouldCorrectlyCalculateEmi(int loanAmount, double yearlyInterestRate, int loanTerm, double expectedEmi) {
        assertEquals(sut.calculate(loanAmount, yearlyInterestRate, loanTerm, VALID_EMAIL), expectedEmi);
    }

    @Test
    void shouldPersistCalculatesEmi() {
        sut.calculate(1, 1.0, 1, VALID_EMAIL);
        verify(emiRepository).persistEmi(0.08378541155580528, VALID_EMAIL);
    }

    static Stream<Arguments> validEmiCalculations() {
        return Stream.of(
                Arguments.of(1, 1.0, 1, 0.08378541155580528),
                Arguments.of(5000, 3.6, 17, 32.80569291821602),
                Arguments.of(753892, 99.2, 29, 62321.738666728386)
        );
    }
}
