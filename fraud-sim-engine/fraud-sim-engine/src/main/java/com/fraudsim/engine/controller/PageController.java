package com.fraudsim.engine.controller;

import com.fraudsim.engine.FraudDetector;
import com.fraudsim.engine.model.Transaction;
import com.fraudsim.engine.repository.TransactionRepository;
import com.fraudsim.engine.service.EmailService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Controller
public class PageController {

    private final FraudDetector fraudDetector;
    private final TransactionRepository transactionRepository;
    private final EmailService emailService;

    // ✅ Only ONE constructor (correct)
    public PageController(FraudDetector fraudDetector,
                          TransactionRepository transactionRepository,
                          EmailService emailService) {

        this.fraudDetector = fraudDetector;
        this.transactionRepository = transactionRepository;
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String home() {
        return "dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/manual-check")
    public String manualCheck() {
        return "manual-check";
    }


    // ✅ Manual Check API (fixed & email added)
    @PostMapping("/manual-check-api")
    @ResponseBody
    public ResponseEntity<?> manualCheckApi(@RequestBody Transaction tx) {

        // Create transaction ID
        if (tx.getTxId() == null || tx.getTxId().isBlank()) {
            tx.setTxId(UUID.randomUUID().toString());
        }

        // Fraud raw score (0–1)
        double rawScore = fraudDetector.score(tx);

        // Convert raw fraud score to percent (0–100)
        double percentScore = rawScore * 100;
        tx.setScore(percentScore);

        // Determine fraud flag
        boolean isFraud = rawScore > 0.6;
        tx.setFraud(isFraud);

        // Save to DB
        transactionRepository.save(tx);

        // 📩 Send Email Notification if FRAUD
        if (isFraud) {
            String message = "⚠ FRAUD ALERT ⚠\n\n" +
                    "A suspicious transaction was detected:\n\n" +
                    "Transaction ID: " + tx.getTxId() + "\n" +
                    "Account ID: " + tx.getAccountId() + "\n" +
                    "Amount: ₹" + tx.getAmount() + "\n" +
                    "Merchant: " + tx.getMerchant() + "\n" +
                    "Channel: " + tx.getChannel() + "\n" +
                    "Location: " + tx.getLocation() + "\n" +
                    "Fraud Score: " + percentScore + "%\n" +
                    "Status: FRAUD\n\n" +
                    "⚠ Please verify this transaction immediately.";

            emailService.sendFraudAlert("your-email@gmail.com", message);
        }

        return ResponseEntity.ok(tx);
    }


    @GetMapping("/history")
    public String historyPage() {
        return "history";
    }

    @GetMapping("/history-data")
    @ResponseBody
    public List<Transaction> historyData() {
        return transactionRepository.findAll();
    }
}
