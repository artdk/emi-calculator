package org.adk.emicalculator.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class EmiCalculationEntity {
    @Id
    private UUID id;

    private double emi;

    private String email;

    public EmiCalculationEntity(double emi, String email) {
        this.id = UUID.randomUUID();
        this.emi = emi;
        this.email = email;
    }

    public double getEmi() {
        return emi;
    }

    public String getEmail() {
        return email;
    }
}
