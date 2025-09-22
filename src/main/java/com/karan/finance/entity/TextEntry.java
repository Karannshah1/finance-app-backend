package com.karan.finance.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "text_entries")
public class TextEntry {

    @Id
    private Integer id;

    @Lob // Specifies that this should be stored as a Large Object.
    @Column(name = "content", columnDefinition = "TEXT") // Ensures the column type can handle large text.
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Standard getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
