package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {

    public ClientDTO getClientDTO(long id);

    public List<ClientDTO> getClientsDTO();

    public Client findByEmail(String email);

    public void saveClient(Client client);

}
