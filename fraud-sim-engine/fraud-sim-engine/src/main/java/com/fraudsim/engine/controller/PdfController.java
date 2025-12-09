package com.fraudsim.engine.controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class PdfController {

    @PostMapping("/download-pdf")
    public ResponseEntity<byte[]> downloadPdf(@RequestBody PdfRequest request) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document pdf = new Document(PageSize.A4);
            PdfWriter.getInstance(pdf, out);

            pdf.open();

            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Font subFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font textFont = new Font(Font.HELVETICA, 12);

            pdf.add(new Paragraph("Fraud Check Report", titleFont));
            pdf.add(new Paragraph("Generated On: " + LocalDateTime.now(), textFont));
            pdf.add(new Paragraph("\n\n"));

            pdf.add(new Paragraph("Transaction Details:", subFont));
            pdf.add(new Paragraph("Transaction ID: " + request.txId, textFont));
            pdf.add(new Paragraph("Account ID: " + request.accountId, textFont));
            pdf.add(new Paragraph("Amount: ₹" + request.amount, textFont));
            pdf.add(new Paragraph("Merchant: " + request.merchant, textFont));
            pdf.add(new Paragraph("Channel: " + request.channel, textFont));
            pdf.add(new Paragraph("Location: " + request.location, textFont));
            pdf.add(new Paragraph("\n"));

            pdf.add(new Paragraph("Fraud Score: " + request.score + "%", textFont));
            pdf.add(new Paragraph("Status: " + (request.fraud ? "FRAUD" : "SAFE"), textFont));
            pdf.add(new Paragraph("\n"));

            pdf.add(new Paragraph("Fraud Indicators:", subFont));

            if (request.reasons != null && !request.reasons.isEmpty()) {
                for (String r : request.reasons) {
                    pdf.add(new Paragraph("- " + r, textFont));
                }
            } else {
                pdf.add(new Paragraph("- No fraud indicators provided.", textFont));
            }

            pdf.close();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fraud-report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    public static class PdfRequest {
        public String txId;
        public String accountId;
        public double amount;
        public String merchant;
        public String channel;
        public String location;
        public double score;
        public boolean fraud;
        public List<String> reasons;
    }
}
