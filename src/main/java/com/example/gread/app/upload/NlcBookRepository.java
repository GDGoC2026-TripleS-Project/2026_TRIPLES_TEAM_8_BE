package com.example.gread.app.upload;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NlcBookRepository extends JpaRepository<NlcBook, Long> {
    boolean existsByExternalId(String externalId);
}
