package com.fraudsim.engine;

import com.fraudsim.engine.model.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.ArrayList;


@Component
public class SimulationEngine {

    private static final List<String> MERCHANTS = Arrays.asList(
            "Amazon", "Flipkart", "Myntra", "Swiggy", "Zomato",
            "Uber", "PaytmMall", "BigBazaar", "CryptoExchange",
            "LuxuryStore", "Lifestyle", "UnknownVendor", "SuspiciousMerchant"
    );

    private static final List<String> CHANNELS = Arrays.asList(
            "Mobile", "Web", "ATM", "POS"
    );

    private static final List<String> LOCATIONS = Arrays.asList(
            "India", "USA", "UK", "Singapore", "UAE",
            "Russia", "Syria", "Iran", "North Korea"
    );

    private final Random random = new Random();

    public List<Transaction> generateTransactions(int count, double fraudRate) {

        List<Transaction> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            Transaction t = new Transaction();

            t.setTxId(UUID.randomUUID().toString());
            t.setAccountId("ACC" + (random.nextInt(900) + 100));
            t.setTimestamp(LocalDateTime.now());

        
            double amount = Math.abs(random.nextGaussian() * 1500) + 200;
            t.setAmount(amount);

            t.setMerchant(MERCHANTS.get(random.nextInt(MERCHANTS.size())));
            t.setChannel(CHANNELS.get(random.nextInt(CHANNELS.size())));
            t.setLocation(LOCATIONS.get(random.nextInt(LOCATIONS.size())));

            
            t.setRecentTxCount(random.nextInt(12));

            t.setFraud(false);

        
            if (Math.random() < fraudRate) {
                t.setFraud(true);

                
                int type = random.nextInt(4);

                switch (type) {
                    case 0: 
                        t.setAmount(amount * 8 + 10000);
                        break;

                    case 1: 
                        t.setMerchant("SuspiciousMerchant");
                        break;

                    case 2: 
                        t.setLocation("North Korea");
                        break;

                    case 3: 
                        t.setRecentTxCount(20);
                        break;
                }
            }

            list.add(t);
        }

        return list;
    }
}
