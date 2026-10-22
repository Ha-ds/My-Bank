package com.mybank.account.service;

import com.mybank.account.dto.ClientDto;
import com.mybank.account.dto.FullClientDto;
import com.mybank.account.exception.ClientBalanceCannotBeNegativeException;
import com.mybank.account.exception.ClientCannotBeNullException;
import com.mybank.account.exception.ClientNotFoundException;
import com.mybank.account.model.AccountType;
import com.mybank.account.model.Client;
import com.mybank.account.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    public void createClient_whenClientExists_clientIsCreated() {
        // Given
        Client client = new Client("Ivan Petrov", "password123", "ivan_petrov@gmail.com");
        List<Client> clients = new ArrayList<>();

        when(clientRepository.getClients()).thenReturn(clients);

        // When
        clientService.createClient(client);

        // Then
        assertEquals(1, clients.size());
        assertEquals(client.getName(), clients.getFirst().getName());
    }

    @Test
    public void createClient_whenClientDoesNotExist_exceptionIsThrown() {
        // Given
        Client client = null;

        // When Then
        assertThrows(ClientCannotBeNullException.class,
                () -> clientService.createClient(client));
    }

    @Test
    public void getClientByIban_whenClientExists_returnsMappedDto() {
        // Given
        Client client = new Client("Ivan Petrov", "password123", "ivan_petrov@gmail.com");
        client.getAccount(AccountType.CURRENT).setBalance(new BigDecimal("150.00"));

        when(clientRepository.getClientByIban(client.getIban())).thenReturn(client);

        // When
        ClientDto result = clientService.getClientByIban(client.getIban());

        // Then
        assertEquals("Ivan Petrov", result.name());
        assertEquals(client.getIban(), result.iban());
        assertEquals(new BigDecimal("150.00"), result.balance());
        assertEquals("ivan_petrov@gmail.com", result.email());
    }

    @Test
    public void getClientByIban_whenClientDoesNotExist_exceptionIsThrown() {
        // Given
        String iban = "NON_EXISTING_IBAN";

        when(clientRepository.getClientByIban(iban))
                .thenThrow(new ClientNotFoundException("Client does not exist"));

        // When Then
        assertThrows(ClientNotFoundException.class,
                () -> clientService.getClientByIban(iban));
    }

    @ParameterizedTest
    @ValueSource(strings = {"500.00", "0"})
    public void updateClient_whenBalanceIsValid_clientIsUpdatedAndSaved(String balance) {
        // Given
        Client client = new Client("Ivan Petrov", "password123", "ivan_petrov@gmail.com");
        ClientDto clientDto = new ClientDto(client.getName(), client.getIban(), new BigDecimal(balance), client.getEmail());

        when(clientRepository.getClientByIban(client.getIban())).thenReturn(client);

        // When
        clientService.updateClient(clientDto);

        // Then
        assertEquals(new BigDecimal(balance), client.getAccount(AccountType.CURRENT).getBalance());
        assertEquals("Ivan Petrov", client.getName());
        assertEquals("ivan_petrov@gmail.com", client.getEmail());
        verify(clientRepository).updateClient(client);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-10", "-0.50"})
    public void updateClient_whenBalanceIsNegative_exceptionIsThrownAndNothingIsSaved(String balance) {
        // Given
        ClientDto clientDto = new ClientDto(
                "Ivan Petrov","SOME_IBAN", new BigDecimal(balance), "ivan_petrov@gmail.com");

        // When Then
        assertThrows(ClientBalanceCannotBeNegativeException.class,
                () -> clientService.updateClient(clientDto));
        verifyNoInteractions(clientRepository);
    }

    @Test
    public void updateClient_whenClientDoesNotExist_exceptionIsThrownAndNothingIsSaved() {
        // Given
        ClientDto clientDto = new ClientDto(
                "Ivan Petrov", "NON_EXISTING_IBAN", new BigDecimal("100"), "ivan_petrov@gmail.com");

        when(clientRepository.getClientByIban("NON_EXISTING_IBAN"))
                .thenThrow(new ClientNotFoundException("Client does not exist"));

        // When Then
        assertThrows(ClientNotFoundException.class,
                () -> clientService.updateClient(clientDto));
        verify(clientRepository, never()).updateClient(any());
    }

    @Test
    public void getAllClients_whenClientsExist_returnsAllMappedDtos() {
        // Given
        Client firstClient = new Client("Ivan Petrov", "password123", "ivan_petrov@gmail.com");
        firstClient.getAccount(AccountType.CURRENT).setBalance(new BigDecimal("100"));
        Client secondClient = new Client("Maria Georgieva", "secret456", "maria@gmail.com");
        secondClient.getAccount(AccountType.CURRENT).setBalance(new BigDecimal("250"));

        when(clientRepository.getClients()).thenReturn(List.of(firstClient, secondClient));

        // When
        List<FullClientDto> result = clientService.getAllClients();

        // Then
        assertEquals(2, result.size());

        assertEquals("Ivan Petrov", result.get(0).name());
        assertEquals(firstClient.getIban(), result.get(0).iban());
        assertEquals(new BigDecimal("100"), result.get(0).balance());
        assertEquals(firstClient.getAccounts(), result.get(0).accounts());
        assertEquals("ivan_petrov@gmail.com", result.get(0).email());

        assertEquals("Maria Georgieva", result.get(1).name());
        assertEquals(secondClient.getIban(), result.get(1).iban());
        assertEquals(new BigDecimal("250"), result.get(1).balance());
        assertEquals(secondClient.getAccounts(), result.get(1).accounts());
        assertEquals("maria@gmail.com", result.get(1).email());
    }
}
