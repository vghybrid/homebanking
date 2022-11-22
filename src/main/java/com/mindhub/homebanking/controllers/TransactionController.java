package com.mindhub.homebanking.controllers;

import com.lowagie.text.DocumentException;
import com.mindhub.homebanking.dtos.FilterTransactionDTO;
import com.mindhub.homebanking.dtos.PDFExportDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://192.168.100.3:8080/")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<?> createNewTransactions(
            Authentication authentication,
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestParam String numberDestiny,
            @RequestParam String numberOrigin)
    {

        Client clientCurrent = clientService.findByEmail(authentication.getName());
        Account accountDestiny = accountService.findByNumber(numberDestiny);
        Account accountOrigin = accountService.findByNumber(numberOrigin);

        Set<Account> accountsClient = clientCurrent.getAccounts();

        if (amount == null) {
            return new ResponseEntity<>("Insufficient amount", HttpStatus.FORBIDDEN);
        }
        if (description.isEmpty()) {
            return new ResponseEntity<>("Missing description data", HttpStatus.FORBIDDEN);
        }
        if (numberDestiny.isEmpty()) {
            return new ResponseEntity<>("Missing Number Destiny data", HttpStatus.FORBIDDEN);
        }
        if (numberOrigin.isEmpty()) {
            return new ResponseEntity<>("Missing Number Origin data", HttpStatus.FORBIDDEN);
        }
        if (numberOrigin.equals(numberDestiny)) {
            return new ResponseEntity<>("The accounts are same", HttpStatus.FORBIDDEN);
        }
        if (accountOrigin == null) {
            return new ResponseEntity<>("The account not exist" ,HttpStatus.FORBIDDEN);
        }
        if (accountsClient.contains(numberOrigin)) {
            return new ResponseEntity<>("Account not authenticated" ,HttpStatus.FORBIDDEN);
        }
        if (accountDestiny == null) {
            return new ResponseEntity<>("The account destiny not exist", HttpStatus.FORBIDDEN);
        }
        if (accountOrigin.getBalance() < amount || amount <= 0 || amount.isNaN()) {
            return new ResponseEntity<>("The account origin does not have enough amount to make the transfer", HttpStatus.FORBIDDEN);
        }

        Double debitBalance = accountOrigin.getBalance() - amount;
        accountOrigin.setBalance(debitBalance);
        accountService.saveAccount(accountOrigin);

        Double creditBalance = accountDestiny.getBalance() + amount;
        accountDestiny.setBalance(creditBalance);
        accountService.saveAccount(accountDestiny);


        Transaction transactionOrigin = new Transaction(description, LocalDateTime.now(), amount, TransactionType.DEBIT, debitBalance);
        Transaction transactionDestiny = new Transaction(description, LocalDateTime.now() , amount, TransactionType.CREDIT, creditBalance);

        accountOrigin.addTransaction(transactionOrigin);
        accountDestiny.addTransaction(transactionDestiny);

        transactionService.saveTransaction(transactionOrigin);
        transactionService.saveTransaction(transactionDestiny);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping ("/transaction/export/pdf")
    public void exportToPDF(
            HttpServletResponse response,
            Authentication authentication,
            @RequestParam String accountNumber)
            throws DocumentException, IOException
    {
        Client clientCurrent = clientService.findByEmail(authentication.getName());
        Account account = accountService.findByNumber(accountNumber);

        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=file.pdf";
        response.setHeader(headerKey, headerValue);

        List<TransactionDTO> transactionDTOList = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
        PDFExportDTO exporter = new PDFExportDTO(transactionDTOList);
        exporter.export(response);

    }
}