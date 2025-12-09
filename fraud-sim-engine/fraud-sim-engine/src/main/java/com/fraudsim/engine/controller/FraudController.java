package com.fraudsim.engine.controller;

import com.fraudsim.engine.SimulationEngine;
import com.fraudsim.engine.FraudDetector;
import com.fraudsim.engine.model.Transaction;
import com.fraudsim.engine.repository.TransactionRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class FraudController {

    private final SimulationEngine simulationEngine;
    private final FraudDetector fraudDetector;
    private final TransactionRepository transactionRepository;

    public FraudController(SimulationEngine simulationEngine,
                           FraudDetector fraudDetector,
                           TransactionRepository transactionRepository) {

        this.simulationEngine = simulationEngine;
        this.fraudDetector = fraudDetector;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/simulate")
    @ResponseBody
    public List<Transaction> simulate(
            @RequestParam(defaultValue = "50") int count,
            @RequestParam(defaultValue = "0.20") double fraudRate) {

        List<Transaction> batch = simulationEngine.generateTransactions(count, fraudRate);

        for (Transaction tx : batch) {
            double score = fraudDetector.score(tx);
            tx.setScore(score);
            tx.setFraud(fraudDetector.isFraud(tx));

            transactionRepository.save(tx); // ⭐ SAVE INTO DB
        }

        return batch; 
    }
}
