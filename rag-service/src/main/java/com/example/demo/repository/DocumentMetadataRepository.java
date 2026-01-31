package com.example.demo.repository;

import com.example.demo.model.DocumentMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentMetadataRepository extends JpaRepository<DocumentMetadata, Long> {
}
