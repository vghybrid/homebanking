package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Loan;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String number;
    private String creationDate;
    private double balance;
    private AccountType type;
    private Set<TransactionDTO> transactions = new HashSet<>();

    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.type = account.getType();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
    }

    public long getId() { return id; }

    public String getNumber() { return number; }

    public AccountType getType() {
        return type;
    }

    public String getCreationDate() { return creationDate; }

    public double getBalance() { return balance; }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setId(long id) { this.id = id;  }

    public void setNumber(String number) { this.number = number; }

    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }

    public void setBalance(double balance) { this.balance = balance; }
}
