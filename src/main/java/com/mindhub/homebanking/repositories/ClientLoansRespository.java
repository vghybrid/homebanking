package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientLoansRespository extends JpaRepository<ClientLoan, Long> {
}
