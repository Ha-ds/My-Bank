package com.mybank.account.service;

import com.mybank.account.dto.ClientDto;
import com.mybank.account.dto.FullClientDto;
import com.mybank.account.exception.ClientBalanceCannotBeNegativeException;
import com.mybank.account.exception.ClientCannotBeNullException;
import com.mybank.account.model.AccountType;
import com.mybank.account.model.Client;
import com.mybank.account.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {

   private final ClientRepository clientRepository;

    /**
     * This method is responsible for creating a new client within the banking system
     */
    public void createClient(Client client) {

        if(client == null) {
            log.error("Create: Client with input null failed to be created");
            throw new ClientCannotBeNullException("Client cannot be null.");
        }
        log.info("Create: Creating new client with id: {}", client.getClientId());
        Client newClient = new Client(client.getName(), client.getPassword(), client.getEmail());
        clientRepository.getClients().add(newClient);
        log.info("Create: New client created");
    }

    /**
     * This method gets a client
     * - the user gives the client iban as an input
     * - this client iban is used by the repository to find the correct client
     * - the client is converted to a ClientDto before sending it as a response
     */
    public ClientDto getClientByIban(String iban) {
        log.info("GetClient: Getting client by iban: {}",  iban);
       Client client = clientRepository.getClientByIban(iban);

        ClientDto clientDto = fromClientEntityToDto(client);
        log.info("GetClient: ClientDto returned successfully");
        return clientDto;
    }

    /**
     *This method updates a client
     * - the user gives ClientDto data as input
     * - the ClientDto is used to find and modify the correct client
     * - after modification client is saved back to the database
     */
    public void updateClient(ClientDto clientDto) {

        if (clientDto.balance().signum() < 0) {

            log.error("Update: Client with iban {} failed to update balance with negative value", clientDto.iban());
            throw new ClientBalanceCannotBeNegativeException("Client balance cannot be negative.");
        }

        log.info("Update: Updating client with given data: {} ", clientDto);
        Client client = clientRepository.getClientByIban(clientDto.iban());
        fromDtoToClient(clientDto, client);
        clientRepository.updateClient(client);
        log.info("Client: Client updated");
    }

    public List<FullClientDto> getAllClients() {
        log.info("GetAll: Getting all clients");
        List<Client> clients  = clientRepository.getClients();
        List<FullClientDto> fullClientDtos = new ArrayList<>();

        for(Client client : clients) {
            fullClientDtos.add(fromClientEntityToFullClientDto(client));
        }
        return fullClientDtos;
    }

    private FullClientDto fromClientEntityToFullClientDto(Client client) {
        return new FullClientDto(
                client.getName(),
                client.getIban(),
                client.getAccount(AccountType.CURRENT).getBalance(),
                client.getAccounts(),
                client.getEmail()
        );
    }

    /**
     * This is a helper method to convert ClientDto input to client data
     */
    private void fromDtoToClient(ClientDto clientDto, Client client) {
        client.setName(client.getName());
        client.setIban(client.getIban());
        client.getAccount(AccountType.CURRENT).setBalance(clientDto.balance());
        client.setEmail(client.getEmail());
    }

    /**
     * This is a helper method to convert client to ClientDto
     */
    private ClientDto fromClientEntityToDto(Client client) {
        return new ClientDto(
                client.getName(),
                client.getIban(),
                client.getAccount(AccountType.CURRENT).getBalance(),
                client.getEmail()
        );
    }
}
