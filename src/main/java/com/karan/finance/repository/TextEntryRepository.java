package com.karan.finance.repository;

import com.karan.finance.entity.TextEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextEntryRepository extends JpaRepository<TextEntry, Integer> {
}

