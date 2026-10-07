package com.mybank.account.repository;

import com.mybank.account.model.Client;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class ClientRepository {

    private final List<Client> clients = new ArrayList<>();

    public Client getClientById(String clientId) {
        for (Client client : clients) {
            if (client.getClientId().equals(clientId)) {
                return client;
            }
        }
        throw new RuntimeException("Client does not exist.");
    }

}
