package com.karan.finance.repository;


import com.karan.finance.entity.SequenceTracker;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SequenceTrackerRepository extends JpaRepository<SequenceTracker, String> {

    /**
     * Finds the sequence tracker by its fixed ID and applies a pessimistic write lock.
     * This is the key to handling concurrency. When one transaction calls this method,
     * it locks the row in the database. Any other concurrent transaction trying to
     * access this same row will be forced to wait until the first one is finished.
     * This prevents two users from getting the same number.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SequenceTracker s WHERE s.id = :id")
    Optional<SequenceTracker> findByIdWithLock(String id);
}

