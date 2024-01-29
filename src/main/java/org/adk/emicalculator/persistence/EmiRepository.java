package org.adk.emicalculator.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmiRepository {

    @Autowired
    private EntityManager entityManager;

    public void persistEmi(double emi, String email) {
        EmiCalculationEntity entity = new EmiCalculationEntity(emi, email);
        entityManager.persist(entity);
    }
}
