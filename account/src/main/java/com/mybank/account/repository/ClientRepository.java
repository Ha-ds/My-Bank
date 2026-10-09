package com.mybank.account.repository;

import com.mybank.account.model.Client;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Component
public class ClientRepository {

    private final List<Client> clients = new ArrayList<>();

    /**
     * This method searches client within the clients' list by given client id as input
     */
    public Client getClientById(String clientId) {
        for (Client client : clients) {
            if (client.getClientId().equals(clientId)) {
                return client;
            }
        }
        log.error("Client: Get client with id {} does not exist", clientId);
        throw new RuntimeException("Client does not exist.");
    }

    /**
     * This method updates client within the clients' list by given client id as input
     */
    public void updateClient(Client client) {
        for(int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getClientId().equals(client.getClientId())) {
                clients.set(i, client);
                return;
            }
        }
        log.error("Client: Update client with id {} does not exist", client.getClientId());
        throw new RuntimeException("Client does not exist.");
    }
}
