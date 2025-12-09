package com.fraudsim.engine.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private String txId;
   private String email;

    private String accountId;
    private double amount;
    private String merchant;
    private String channel;
    private String location;

    private int recentTxCount;
    private double score;
    private boolean fraud;

    private LocalDateTime timestamp = LocalDateTime.now();
}
