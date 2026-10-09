package com.mybank.account.service;

import com.mybank.account.dto.ClientDto;
import com.mybank.account.model.AccountType;
import com.mybank.account.model.Client;
import com.mybank.account.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {

   private final ClientRepository clientRepository;

    /**
     * This method is responsible for creating a new client within the banking system
     */
    public void createClient(Client client) {
        log.info("Client: Creating new client with id: {}", client.getClientId());
        Client newClient = new Client(client.getClientId(), client.getName(), client.getIban(), client.getPassword());
        clientRepository.getClients().add(newClient);
        log.info("Client: New client created");
    }

    /**
     * This method gets a client
     * - the user gives the client id as an input
     * - this client id is used by the repository to find the correct client
     * - the client is converted to a ClientDto before sending it as a response
     */
    public ClientDto getClientById(String clientId) {
        log.info("Client: Getting client by id: {}",  clientId);
       Client client = clientRepository.getClientById(clientId);
        ClientDto clientDto = fromClientEntityToDto(client);
        log.info("Client: ClientDto returned successfully");
        return clientDto;
    }

    /**
     *This method updates a client
     * - the user gives ClientDto data as input
     * - the ClientDto is used to find and modify the correct client
     * - after modification client is saved back to the database
     */
    public void updateClient(ClientDto clientDto) {
        log.info("Client: Updating client with given data: {} ", clientDto);
        Client client = clientRepository.getClientById(clientDto.clientId());
        fromDtoToClient(clientDto, client);
        clientRepository.updateClient(client);
        log.info("Client: Client updated");
    }

    /**
     * This is a helper method to convert ClientDto input to client data
     */
    private void fromDtoToClient(ClientDto clientDto, Client client) {
        client.setName(clientDto.name());
        client.setIban(clientDto.iban());
        client.getAccount(AccountType.CURRENT).setAmount(clientDto.amount());
    }

    /**
     * This is a helper method to convert client to ClientDto
     */
    private ClientDto fromClientEntityToDto(Client client) {
        ClientDto clientDto = new ClientDto(
                client.getClientId(),
                client.getName(),
                client.getIban(),
                client.getAccount(AccountType.CURRENT).getAmount()
        );
        return clientDto;
    }
}
