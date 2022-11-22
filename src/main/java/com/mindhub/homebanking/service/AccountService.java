package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;
import java.util.Set;

public interface AccountService {

    public AccountDTO getAccountDTO(long id);

    public List<AccountDTO> getAccountsDTO();

    public Set<String> accountsNumbers();

    public void saveAccount(Account account);

    public Account findByNumber(String number);

    public void deleteAccount(Account account);

}
