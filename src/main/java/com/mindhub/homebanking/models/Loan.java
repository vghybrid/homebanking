package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;
    private String name;
    private Double maxAmount;
    private double interest;

    @ElementCollection
    @Column(name="loans")
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy="loan", fetch= FetchType.EAGER)
    Set<ClientLoan> clientLoan = new HashSet<>();

    public Loan() {
    }

    public Loan(String name, double maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    public Loan(long id, String name, Double maxAmount, double interest, List<Integer> payments, Set<ClientLoan> clientLoan) {
        this.id = id;
        this.name = name;
        this.maxAmount = maxAmount;
        this.interest = interest;
        this.payments = payments;
        this.clientLoan = clientLoan;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    @JsonIgnore
    public List<Client> getClients() {
        return clientLoan.stream().map(client -> client.getClient()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxAmount=" + maxAmount +
                ", payments=" + payments +
                '}';
    }
}
