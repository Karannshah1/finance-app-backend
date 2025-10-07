package com.karan.finance.controller;


import com.karan.finance.dto.TextRequest;
import com.karan.finance.dto.TextResponse;
import com.karan.finance.service.TextStorageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sender")
@Validated // Enables validation for path variables and request parameters
public class TextController {

    private final TextStorageService textStorageService;

    @Autowired
    public TextController(TextStorageService textStorageService) {
        this.textStorageService = textStorageService;
    }

    @GetMapping
    public ResponseEntity<TextResponse> healthCheck() {
        
        return ResponseEntity.ok("Health is ok.");
    }

    @PostMapping("/text")
    public ResponseEntity<TextResponse> saveText(@Valid @RequestBody TextRequest request) {
        int assignedNumber = textStorageService.saveText(request.getText());
        return ResponseEntity.ok(new TextResponse(assignedNumber));
    }

    @GetMapping("/text/{number}")
    public ResponseEntity<String> getText(
            @PathVariable @Min(1) @Max(9999) int number) {
        String content = textStorageService.getText(number);
        return ResponseEntity.ok(content);
    }
}

