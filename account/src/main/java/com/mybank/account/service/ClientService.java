package com.mybank.account.service;

import com.mybank.account.model.Client;
import com.mybank.account.repository.ClientRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

   private final ClientRepository clientRepository;

    public void createClient(Client client) {
        Client newClient = new Client(client.getName(), client.getPassword());
        clientRepository.getClients().add(newClient);
    }

    public Client getClientById(String clientId){
        return clientRepository.getClientById(clientId);
    }
}
