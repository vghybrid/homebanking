package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;

    private String description;
    private String date;
    private LocalDateTime transactionDate;
    private Double amount;
    private TransactionType type;
    private Double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;

    public Transaction() {
    }

    public Transaction(String description, String date, Double amount, TransactionType type) {
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    public Transaction(String description, String date, Double amount, TransactionType type, Double balance) {
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.balance = balance;
    }

    public Transaction(String description, LocalDateTime transactionDate, Double amount, TransactionType type, Double balance) {
        this.description = description;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.type = type;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                ", type=" + type +
                '}';
    }
}
