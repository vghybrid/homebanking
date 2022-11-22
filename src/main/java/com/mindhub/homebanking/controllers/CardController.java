package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardPaymentsDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://192.168.100.3:8080/")
public class CardController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    public String numberRandom(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min) + "-" + (int) ((Math.random() * (max - min)) + min) + "-" + (int) ((Math.random() * (max - min)) + min) + "-" + (int) ((Math.random() * (max - min)) + min);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<?> createCard(Authentication authentication,
                                        @RequestParam CardType cardType,
                                        @RequestParam CardColor cardColor)
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime years = LocalDateTime.now().plusYears(5);
        DateTimeFormatter format =  DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = now.format(format);
        String formatDateTimeYears = years.format(format);

        Client clientCurrent = clientService.findByEmail(authentication.getName());

        Set<Card> cards = clientCurrent.getCards().stream().filter(card -> card.getType() == cardType).collect(Collectors.toSet());

        if (cards.size() == 3) {
            return new ResponseEntity<>("You can't have more than 3 cards of the same type" + cardType, HttpStatus.FORBIDDEN);
        }
        if (cardColor == null ){
            return new ResponseEntity<>("The color of the card is invalid, the valid colors are SILVER, GOLD or TITANIUM", HttpStatus.FORBIDDEN);
        }
        if (cardType == null){
            return new ResponseEntity<>("The card type is invalid, the valid card types are: DEBIT or CREDIT", HttpStatus.FORBIDDEN);
        }

        String fullName = clientCurrent.getFirstName()+ " " + clientCurrent.getLastName();
        Integer cvv = (int) (Math.random() * 1000 - 100) + 100;
        String number = numberRandom(1000, 10000);
        Card newCard = new Card(fullName, cardType, cardColor, number, cvv, formatDateTime, formatDateTimeYears);
        clientCurrent.addCard(newCard);
        cardService.saveCard(newCard);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PatchMapping("/clients/current/cards")
    public ResponseEntity<Object> deleteCard(@RequestParam String number, Authentication authentication){
        Client clientCurrent = this.clientService.findByEmail(authentication.getName());
        Card cardToDelete = this.cardService.findByNumber(number);

        if(!clientCurrent.getCards().contains(cardToDelete)){
            return new ResponseEntity<>("This card doesn't belong to this client", HttpStatus.FORBIDDEN);
        }

        if(cardToDelete == null){
            return new ResponseEntity<>("The card you are trying to delete does not exist",HttpStatus.FORBIDDEN);
        }

        cardService.deleteCard(cardToDelete);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/clients/current/cards/payment")
    public ResponseEntity<Object> makeCardTransaction(@RequestBody CardPaymentsDTO cardPaymentDTO,
                                                      Authentication authentication){

        Card card = this.cardService.findByNumber(cardPaymentDTO.getCardNumber());
        Account account = this.accountService.findByNumber(cardPaymentDTO.getAccountNumber());
        Client client = card.getClient();

        double amount = cardPaymentDTO.getAmount();
        String description = cardPaymentDTO.getDescription();
        int cvv = cardPaymentDTO.getCvv();
        String cardNumber = cardPaymentDTO.getCardNumber();


        if( description.isEmpty() || amount <= 0 || cardNumber.isEmpty())
        {
            return new ResponseEntity<>("Complete all the fields", HttpStatus.FORBIDDEN);
        }

        if(!client.getCards().contains(card)){
            return new ResponseEntity<>("This card doesn't belong to this client", HttpStatus.FORBIDDEN);
        }

        if(card == null)
        {
            return new ResponseEntity<>("The card number you select doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(account))
        {
            return new ResponseEntity<>("The account you select do not belong to the client", HttpStatus.FORBIDDEN);
        }

        if(cvv != card.getCvv())
        {
            return new ResponseEntity<>("The CVV you select isn't correct",HttpStatus.FORBIDDEN);
        }
        if(account.getBalance() < amount)
        {
            return new ResponseEntity<>("There isn't enough balance to make this transaction",HttpStatus.FORBIDDEN);
        }


        Double debitBalance = account.getBalance() - amount;
        account.setBalance(debitBalance);
        accountService.saveAccount(account);
        

        Transaction cardTransaction = new Transaction(description, LocalDateTime.now(), amount,TransactionType.DEBIT, debitBalance);
        account.addTransaction(cardTransaction);
        transactionService.saveTransaction(cardTransaction);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
