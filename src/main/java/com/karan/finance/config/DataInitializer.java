package com.karan.finance.config;


import com.karan.finance.entity.SequenceTracker;
import com.karan.finance.repository.SequenceTrackerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final SequenceTrackerRepository sequenceTrackerRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        final String sequenceId = "TEXT_ENTRY_SEQUENCE";

        // Check if the sequence tracker already exists to prevent re-creating it on every restart.
        if (!sequenceTrackerRepository.existsById(sequenceId)) {
            logger.info("Sequence tracker not found. Initializing new sequence...");
            SequenceTracker initialSequence = new SequenceTracker();
            initialSequence.setId(sequenceId);
            initialSequence.setNextNumber(1); // Start the sequence at number 1.
            sequenceTrackerRepository.save(initialSequence);
            logger.info("Successfully initialized sequence tracker with ID '{}'", sequenceId);
        } else {
            logger.info("Sequence tracker with ID '{}' already exists. Skipping initialization.", sequenceId);
        }
    }
}
