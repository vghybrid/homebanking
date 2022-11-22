package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accounts = new HashSet<>();
    private List<ClientLoanDTO> loans = new ArrayList<>();
    private Set<CardDTO> cards = new HashSet<>();

    public ClientDTO() {
    }

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
        this.loans = client.getClientLoan().stream().map(loan -> new ClientLoanDTO(loan)).collect(Collectors.toList());
        this.cards = client.getCards().stream().map(card -> new  CardDTO(card)).collect(Collectors.toSet());
    }

    public long getId() { return id; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getEmail() { return email; }

    public Set<AccountDTO> getAccounts() { return accounts; }

    public List<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setEmail(String email) { this.email = email; }
}
