package com.PDFGenerator.Controller;

import com.PDFGenerator.Model.PdfRequest;
import com.PDFGenerator.Service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {
    @Autowired
    private PdfService pdfService;

    @PostMapping("/generate")
    public ResponseEntity<InputStreamResource> generatePdf(@RequestBody PdfRequest request) {
        try {
            String contentHash = calculateContentHash(request);
            Optional<String> existingFileName =
                    pdfService.findExistingPdf(contentHash);

            ByteArrayInputStream pdf;
            if (existingFileName.isPresent()) {
                pdf = pdfService.getPdfFromStorage(existingFileName.get());
            }
            else{
                pdf = pdfService.generatePdf(request);
                pdfService.storePdf(pdf, contentHash);
                pdf.reset();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=invoice.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdf));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private String calculateContentHash (PdfRequest request) {
        return DigestUtils.md5Hex(request.toString());
    }
}
