package com.PDFGenerator.Service;

import com.PDFGenerator.Model.Item;
import com.PDFGenerator.Model.PdfDocument;
import com.PDFGenerator.Model.PdfRequest;
import com.PDFGenerator.Repository.PdfDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PdfServiceTest {
    @Autowired
    private PdfService pdfService;

    @MockBean
    private PdfDocumentRepository pdfDocumentRepository;

    private PdfRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleRequest = new PdfRequest();
        sampleRequest.setSeller("XYZ Pvt. Ltd.");
        sampleRequest.setBuyer("ABC Computers");
        Item item = new Item();
        item.setName("Product 1");
        item.setQuantity("12 Nos");
        item.setRate(123.00);
        item.setAmount(1476.00);
        sampleRequest.setItems(Collections.singletonList(item));
    }

    @Test
    void testPdfGenerationAndStorage() throws Exception {
        ByteArrayInputStream pdfStream = pdfService.generatePdf(sampleRequest);
        assertNotNull(pdfStream);
        assertTrue(pdfStream.available() > 0);

        String contentHash = "sampleHash";
        when(pdfDocumentRepository.save(any())).thenReturn(null);

        String fileName = pdfService.storePdf(pdfStream, contentHash);
        assertNotNull(fileName);
        assertTrue(fileName.endsWith(".pdf"));

        verify(pdfDocumentRepository, times(1)).save(any());
    }

    @Test
    void testFindExistingPdf() {
        String contentHash = "existingHash";
        PdfDocument mockDocument = new PdfDocument();
        mockDocument.setFileName("invoice.pdf");
        when(pdfDocumentRepository.findByContentHash(contentHash))
                .thenReturn(Optional.of(mockDocument));

        Optional<String> result = pdfService.findExistingPdf(contentHash);
        assertTrue(result.isPresent());
        assertEquals("invoice.pdf", result.get());

        verify(pdfDocumentRepository, times(1)).findByContentHash(contentHash);
    }
}
