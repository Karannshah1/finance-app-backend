package com.karan.finance.service;


import com.karan.finance.exception.ResourceNotFoundException;
import com.karan.finance.entity.SequenceTracker;
import com.karan.finance.entity.TextEntry;
import com.karan.finance.repository.SequenceTrackerRepository;
import com.karan.finance.repository.TextEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TextStorageService {

    private static final String SEQUENCE_ID = "TEXT_ENTRY_SEQUENCE";
    private static final int MAX_NUMBER = 9999;
    private static final int START_NUMBER = 1;

    private final TextEntryRepository textEntryRepository;
    private final SequenceTrackerRepository sequenceTrackerRepository;

    @Autowired
    public TextStorageService(TextEntryRepository textEntryRepository, SequenceTrackerRepository sequenceTrackerRepository) {
        this.textEntryRepository = textEntryRepository;
        this.sequenceTrackerRepository = sequenceTrackerRepository;
    }

    /**
     * Saves text and returns the assigned number. This method is transactional and thread-safe.
     * It uses a database-level lock to manage the sequence for number generation,
     * ensuring correctness even with many parallel users.
     */
    @Transactional
    public int saveText(String content) {
        // Find the sequence tracker and lock it for the duration of this transaction.
        SequenceTracker sequence = sequenceTrackerRepository.findByIdWithLock(SEQUENCE_ID)
                .orElseThrow(() -> new IllegalStateException("SequenceTracker not initialized in the database."));

        int numberToUse = sequence.getNextNumber();

        // Create or update the text entry.
        TextEntry entry = textEntryRepository.findById(numberToUse)
                .orElse(new TextEntry()); // If it doesn't exist, create a new one.

        entry.setId(numberToUse);
        entry.setContent(content);
        entry.setCreatedAt(LocalDateTime.now());
        textEntryRepository.save(entry);

        // Update the next number for the next user, wrapping around if necessary.
        int nextNumber = numberToUse + 1;
        if (nextNumber > MAX_NUMBER) {
            nextNumber = START_NUMBER; // Wrap around to 1
        }
        sequence.setNextNumber(nextNumber);
        sequenceTrackerRepository.save(sequence);

        return numberToUse;
    }

    /**
     * Retrieves text by its assigned number.
     */
    @Transactional(readOnly = true)
    public String getText(int number) {
        return textEntryRepository.findById(number)
                .map(TextEntry::getContent)
                .orElseThrow(() -> new ResourceNotFoundException("No text found for number: " + number));
    }
}
