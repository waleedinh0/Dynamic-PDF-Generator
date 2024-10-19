package com.PDFGenerator.Service;
import com.PDFGenerator.Model.Item;
import com.PDFGenerator.Model.PdfDocument;
import com.itextpdf.text.*;
import com.PDFGenerator.Model.PdfRequest;
import com.PDFGenerator.Repository.PdfDocumentRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class PdfService {
    @Autowired
    private PdfDocumentRepository pdfDocumentRepository;

    private static final String PDF_STORAGE_PATH = "pdf_storage/";

    public ByteArrayInputStream generatePdf (PdfRequest request) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        addPartyInfoTable(document, request);

        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        addTableHeader(table);
        addRows(table, request.getItems());
        document.add(table);

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    public String storePdf (ByteArrayInputStream pdfStream, String contentHash) throws IOException {
        String fileName = UUID.randomUUID().toString() + ".pdf";
        Files.createDirectories(Paths.get(PDF_STORAGE_PATH));
        Files.copy(pdfStream, Paths.get(PDF_STORAGE_PATH + fileName),
                StandardCopyOption.REPLACE_EXISTING);

        PdfDocument pdfDocument = new PdfDocument();
        pdfDocument.setFileName(fileName);
        pdfDocument.setContentHash(contentHash);
        pdfDocumentRepository.save(pdfDocument);

        return fileName;
    }

    public Optional<String> findExistingPdf (String contentHash) {
        return pdfDocumentRepository.findByContentHash(contentHash)
                .map(PdfDocument::getFileName);
    }

    public ByteArrayInputStream getPdfFromStorage(String fileName) throws IOException {
        Path path = Paths.get(PDF_STORAGE_PATH + fileName);
        return new ByteArrayInputStream(Files.readAllBytes(path));
    }

    private void addPartyInfoTable(Document document, PdfRequest request) throws DocumentException {
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);

        PdfPCell sellerCell = new PdfPCell();
        sellerCell.addElement(new Paragraph("Seller:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        sellerCell.addElement(new Paragraph(request.getSeller()));
        sellerCell.addElement(new Paragraph(request.getSellerAddress()));
        sellerCell.addElement(new Paragraph("GSTIN: " + request.getSellerGstIn()));
        sellerCell.setBorder(Rectangle.BOX);
        headerTable.addCell(sellerCell);

        PdfPCell buyerCell = new PdfPCell();
        buyerCell.addElement(new Paragraph("Buyer:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        buyerCell.addElement(new Paragraph(request.getBuyer()));
        buyerCell.addElement(new Paragraph(request.getBuyerAddress()));
        buyerCell.addElement(new Paragraph("GSTIN: " + request.getBuyerGstIn()));
        buyerCell.setBorder(Rectangle.BOX);
        headerTable.addCell(buyerCell);

        document.add(headerTable);
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Item", "Quantity", "Rate", "Amount")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, java.util.List<Item> items) {
        for (Item item : items) {
            addCellWithAlignment(table, item.getName(), Element.ALIGN_LEFT);
            addCellWithAlignment(table, item.getQuantity(), Element.ALIGN_CENTER);
            addCellWithAlignment(table, String.valueOf(item.getRate()), Element.ALIGN_RIGHT);
            addCellWithAlignment(table, String.valueOf(item.getAmount()), Element.ALIGN_RIGHT);
        }
    }

    private void addCellWithAlignment(PdfPTable table, String content, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setHorizontalAlignment(alignment);
        table.addCell(cell);
    }
}
