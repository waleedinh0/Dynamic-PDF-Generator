package com.PDFGenerator.Repository;

import com.PDFGenerator.Model.PdfDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PdfDocumentRepository extends JpaRepository<PdfDocument, Long> {
    Optional<PdfDocument> findByContentHash(String contentHash);

}
