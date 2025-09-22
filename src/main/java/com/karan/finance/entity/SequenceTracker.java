package com.karan.finance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sequence_tracker")
public class SequenceTracker {

    @Id
    private String id; // A fixed ID, e.g., "TEXT_ENTRY_SEQUENCE"

    private int nextNumber;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNextNumber() {
        return nextNumber;
    }

    public void setNextNumber(int nextNumber) {
        this.nextNumber = nextNumber;
    }
}
