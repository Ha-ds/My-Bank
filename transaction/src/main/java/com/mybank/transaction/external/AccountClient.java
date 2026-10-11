package com.mybank.transaction.external;

import com.mybank.transaction.model.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AccountClient {

    RestTemplate restTemplate;

    public Client getClientById(String clientId) {
        return restTemplate.getForEntity(
                        "localhost:8080/account/get-client/" + clientId,
                        Client.class)
                .getBody();
    }
}
