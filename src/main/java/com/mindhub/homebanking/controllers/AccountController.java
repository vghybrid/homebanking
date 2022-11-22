package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://192.168.100.3:8080/")
public class AccountController {
   @Autowired
   private ClientService clientService;
   @Autowired
   private AccountService accountService;
   @Autowired
   private TransactionService transactionService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccountsDTO();
    }

    @GetMapping("accounts/{id}")
    public AccountDTO getAccount(@PathVariable long id){
        return accountService.getAccountDTO(id);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<?> createNewAccount(Authentication authentication, @RequestParam AccountType type) {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format =  DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = now.format(format);

        Client clientCurrent = clientService.findByEmail(authentication.getName());

        if (clientCurrent.getAccounts().size() > 3) {
            return new ResponseEntity<>("You already have 3 accounts", HttpStatus.FORBIDDEN);
        }else {
            long numberAccount = (long) (Math.random() * 100000000 - 1 ) + 1;
            String accountNumber = "VIN" + numberAccount;
            Account newAccount = new Account(accountNumber, formatDateTime, 0, type);
            clientCurrent.addAccount(newAccount);
            accountService.saveAccount(newAccount);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @PatchMapping("/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount(@RequestParam String number, Authentication authentication){
        Account accountToDelete = this.accountService.findByNumber(number);
        Client clientCurrent = this.clientService.findByEmail(authentication.getName());
        Set<Transaction> transactionToDelete = accountToDelete.getTransactions();

        if(!clientCurrent.getAccounts().contains(accountToDelete)){
            return new ResponseEntity<>("This account doesn't belong to this client", HttpStatus.FORBIDDEN);
        }

        if(accountToDelete == null){
            return new ResponseEntity<>("The account you are trying to delete does not exist",HttpStatus.FORBIDDEN);
        }

        if(accountToDelete.getBalance() > 0){
            return new ResponseEntity<>("The account you are trying to delete does not have an empty balance",HttpStatus.FORBIDDEN);
        }

        transactionService.deleteAllTransactions(transactionToDelete);
        accountService.deleteAccount(accountToDelete);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
