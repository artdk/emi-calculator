package org.adk.emicalculator.persistence;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(EmiRepository.class)
class EmiRepositoryTest {
    @Autowired
    private EmiRepository sut;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldPersistEmiCalculation() {
        double emi = 19.5;
        String email = "j.doe@gmail.com";
        sut.persistEmi(emi, email);

        List<EmiCalculationEntity> persistedEntities = entityManager.createQuery("select emi from EmiCalculationEntity emi", EmiCalculationEntity.class).getResultList();
        assertEquals(persistedEntities.size(), 1);
        assertEquals(persistedEntities.getFirst().getEmi(), emi);
        assertEquals(persistedEntities.getFirst().getEmail(), email);
    }
}
