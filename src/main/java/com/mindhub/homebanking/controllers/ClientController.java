package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://192.168.100.3:8080/")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClientsDTO();
    }

    @GetMapping("clients/{id}")
    public ClientDTO getClient(@PathVariable long id) {
        return clientService.getClientDTO(id);
    }

    @PostMapping("/clients")
    public ResponseEntity<?> registerClient(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password)
    {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format =  DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = now.format(format);

        if (firstName.isEmpty()) {
            return new ResponseEntity<>("Missing first name data", HttpStatus.FORBIDDEN);
        }
        if(lastName.isEmpty()){
            return new ResponseEntity<>("Missing last name data", HttpStatus.FORBIDDEN);
        }
        if(email.isEmpty()){
            return new ResponseEntity<>("Missing email data", HttpStatus.FORBIDDEN);
        }
        if(password.isEmpty()){
            return new ResponseEntity<>("Missing password data", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email) != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(newClient);

        Set<String> accountsNumber = accountService.accountsNumbers();
        long numberRandom;
        do{
            numberRandom = (long) (Math.random() * 1000000000 - 1) + 1;
        } while(accountsNumber.equals("VIN" + numberRandom));
        String accountNumber = "VIN" + numberRandom;
        Account newAccount = new Account(accountNumber, formatDateTime,0, AccountType.CURRENT);
        newClient.addAccount(newAccount);
        accountService.saveAccount(newAccount);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getOneClient(Authentication authentication) {
        // Client clientCurrent = clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }




}
