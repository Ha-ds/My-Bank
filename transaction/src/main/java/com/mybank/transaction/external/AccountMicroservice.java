package com.mybank.transaction.external;

import com.mybank.transaction.model.Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountMicroservice {

    RestTemplate restTemplate;

    public Client getClientByIban(String clientIban) {
        log.info("Account: Getting client by iban {}", clientIban);
        return restTemplate.getForEntity(
                        "localhost:8080/account/get-client/" + clientIban,
                        Client.class)
                .getBody();
    }

    public void updateClientByIban(Client client) {
        log.info("Account: Updating client by iban {}", client.getIban());
        restTemplate.postForEntity(
                "localhost:8080/account/update-client", client, Client.class);

    }
}
