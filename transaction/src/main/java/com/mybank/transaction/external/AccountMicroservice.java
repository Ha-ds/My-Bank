package com.mybank.transaction.external;

import com.mybank.transaction.model.ClientDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountMicroservice {

    private final RestTemplate restTemplate;

    public ClientDto getClientByIban(String iban) {
        try{
        log.info("Account: Getting client by iban {}", iban);
        return restTemplate.getForEntity(
                        "http://localhost:8080/account/get-client/" + iban,
                        ClientDto.class)
                .getBody();
        } catch (Exception exception){
            log.error("GetClient: Exception with cause: {}", exception.getMessage());
            throw new RuntimeException();
        }
    }

    public void updateClientByIban(ClientDto clientDto) {
        log.info("Account: Updating client by iban {}", clientDto.getIban());
        restTemplate.put(
                "http://localhost:8080/account/update-client", clientDto, ClientDto.class);

    }
}
