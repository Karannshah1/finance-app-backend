package com.karan.finance.dto;

import jakarta.validation.constraints.NotBlank;

public class TextRequest {

    @NotBlank(message = "Text content cannot be empty.")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
