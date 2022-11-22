package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Transaction;

import java.util.Set;

public interface TransactionService {

    public void saveTransaction(Transaction transaction);
    public void deleteAllTransactions(Set<Transaction> transaction);

}
