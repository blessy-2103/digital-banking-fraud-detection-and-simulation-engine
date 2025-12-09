package com.fraudsim.engine;

import com.fraudsim.engine.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class FraudDetector {

    // High-risk merchants (simulates MCC fraud categories)
    private static final List<String> HIGH_RISK_MERCHANTS = Arrays.asList(
            "CryptoExchange", "LuxuryStore", "SuspiciousMerchant", "UnknownVendor"
    );

    // High-risk locations (geographical anomalies)
    private static final List<String> HIGH_RISK_LOCATIONS = Arrays.asList(
            "Russia", "North Korea", "Iran", "Syria"
    );

    // Main scoring function (0.0 to 1.0)
    public double score(Transaction tx) {
        double score = 0;

        // RULE 1: Very large amount
        if (tx.getAmount() > 20000) score += 0.45;
        else if (tx.getAmount() > 10000) score += 0.30;
        else if (tx.getAmount() > 5000) score += 0.15;

        // RULE 2: High-risk merchant category
        if (HIGH_RISK_MERCHANTS.contains(tx.getMerchant())) {
            score += 0.25;
        }

        // RULE 3: Location mismatch (login vs transaction)
        if (HIGH_RISK_LOCATIONS.contains(tx.getLocation())) {
            score += 0.25;
        }

        // RULE 4: Suspicious channel behavior
        if (tx.getChannel().equalsIgnoreCase("ATM") && tx.getAmount() > 5000)
            score += 0.20;

        if (tx.getChannel().equalsIgnoreCase("WEB") && tx.getAmount() > 15000)
            score += 0.20;

        // RULE 5: Velocity rule — too many transactions quickly
        if (tx.getRecentTxCount() > 5)
            score += 0.20;

        if (tx.getRecentTxCount() > 10)
            score += 0.35;

        // RULE 6: Random noise factor (simulate real-life variability)
        score += Math.random() * 0.10;

        // Cap score
        return Math.min(1.0, score);
    }

    // Flag as fraud if risk score >= threshold
    public boolean isFraud(Transaction tx) {
        return score(tx) >= 0.60;  // Threshold recommended for Infosys project
    }
}
