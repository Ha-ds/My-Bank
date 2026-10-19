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
     * This method searches client within the clients' list by given client iban as input
     */
    public Client getClientByIban(String iban) {
        for (Client client : clients) {
            if (client.getIban().equals(iban)) {
                return client;
            }
        }
        log.error("GetClient: Get client with iban {} does not exist", iban);
        throw new RuntimeException("Client does not exist.");
    }

    /**
     * This method updates client within the clients' list by given client id as input
     */
    public void updateClient(Client client) {
        for(int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getIban().equals(client.getIban())) {
                clients.set(i, client);
                return;
            }
        }
        log.error("Update: Update client with iban {} does not exist", client.getIban());
        throw new RuntimeException("Client does not exist.");
    }
}
