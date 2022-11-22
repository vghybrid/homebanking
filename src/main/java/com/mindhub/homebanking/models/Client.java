package com.mindhub.homebanking.models;

import com.mindhub.homebanking.utils.ClientUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;


    @OneToMany(mappedBy="client", fetch= FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy="client", fetch= FetchType.EAGER)
    Set<ClientLoan> clientLoan = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<Card> cards = new HashSet<>();

    public Client() {
    }

    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public Set<ClientLoan> getClientLoan() {
        return clientLoan;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public List<Loan> getLoans() {
        return clientLoan.stream().map(loan -> loan.getLoan()).collect(Collectors.toList());
    }

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public void addCard(Card card) {
        card.setClient(this);
        cards.add(card);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", accounts=" + accounts +
                ", clientLoan=" + clientLoan +
                ", cards=" + cards +
                '}';
    }


}
