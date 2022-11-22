package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private long idLoan;
    private Double amount;
    private Integer payments;
    private String numberAccount;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(long idLoan, Double amount, Integer payments, String numberAccount) {
        this.idLoan = idLoan;
        this.amount = amount;
        this.payments = payments;
        this.numberAccount = numberAccount;
    }

    public long getIdLoan() {
        return idLoan;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getNumberAccount() {
        return numberAccount;
    }
}
