package com.mindhub.homebanking.service.implementation;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoansRespository;
import com.mindhub.homebanking.service.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImplement implements ClientLoanService {

    @Autowired
    private ClientLoansRespository clientLoansRespository;

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoansRespository.save(clientLoan);
    }
}
