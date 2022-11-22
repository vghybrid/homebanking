package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://192.168.100.3:8080/")
public class LoanController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientLoanService clientLoanService;

    @GetMapping("/loans")
    public List<LoanDTO> getListLoanDTO() {
        return loanService.getLoansDTO();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<?> newLoan(
            Authentication authentication,
            @RequestBody LoanApplicationDTO loanApplicationDTO)
    {
        long idLoan = loanApplicationDTO.getIdLoan();
        String numberAccount = loanApplicationDTO.getNumberAccount();

        Client clientCurrent = clientService.findByEmail(authentication.getName());
        Account account = accountService.findByNumber(numberAccount);
        Loan loan = loanService.getLoanId(idLoan);

        Account accountOrigin = accountService.findByNumber(account.getNumber());
        Account accountDestiny = accountService.findByNumber(account.getNumber());

        Set<ClientLoan> clientLoan = clientCurrent.getClientLoan();

        Double amountInterest = null;

        // chekeando que exista el cliente authenticado
        if (clientCurrent == null) {
            return new ResponseEntity<>("C",HttpStatus.FORBIDDEN);
        }

        // chekeando si alguno de los datos falta o esta vacio
        if (loanApplicationDTO.getNumberAccount().isEmpty()) {
            return new ResponseEntity<>("Missing number account data",HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getPayments() <= 0 ) {
            return new ResponseEntity<>("Payments cannot be equal to or less than 0",HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getIdLoan() <= 0) {
            return new ResponseEntity<>("A",HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getAmount().isNaN()) {
            return new ResponseEntity<>("Amount cannot be equal to or less than 0", HttpStatus.FORBIDDEN);
        }

        // chekeando si el prestamo es nulo
        if (loan == null) {
            return new ResponseEntity<>("Loan data cannot be verify", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("Exceed the maximum amount available or the amount is negative", HttpStatus.FORBIDDEN);
        }

        // chekeando si la cuenta existe
        if (account == null) {
            return new ResponseEntity<>("The account data is not correct", HttpStatus.FORBIDDEN);
        }

        // chekeando si los datos de la cuenta pertenece a un cliente autenticado
        if (!clientCurrent.getAccounts().contains(account)) {
            return new ResponseEntity<>("Unable to Authenticate accounts data", HttpStatus.FORBIDDEN);
        }
        if (clientLoan.stream().filter(clientLoans -> clientLoans.getLoan().getName().equals(loan.getName())).toArray().length == 1) {
            return new ResponseEntity<>("B",HttpStatus.FORBIDDEN);
        }



        if (loan.getName().equals("Mortgage")) {
            amountInterest = loanApplicationDTO.getAmount() * 1.20;
        }

        if (loan.getName().equals("Automotive")) {
            amountInterest = loanApplicationDTO.getAmount() * 1.15;
        }

        if (loan.getName().equals("Personal")) {
            amountInterest = loanApplicationDTO.getAmount() * 1.10;
        }



        ClientLoan clientLoanCurrent = new ClientLoan(loanApplicationDTO.getPayments(), amountInterest, clientCurrent, loan);
        Transaction loanTransaction = new Transaction(loan.getName() + " Loan Approved", LocalDateTime.now(), loanApplicationDTO.getAmount(), TransactionType.CREDIT, loanApplicationDTO.getAmount() + account.getBalance());

        clientLoanService.saveClientLoan(clientLoanCurrent);
        accountDestiny.addTransaction(loanTransaction);
        transactionService.saveTransaction(loanTransaction);

        account.setBalance(loanApplicationDTO.getAmount() + account.getBalance());
        accountService.saveAccount(account);

        return new ResponseEntity<>(HttpStatus.CREATED);


    }

}
