package com.mybank.account.service;

import com.mybank.account.exception.ClientCannotBeNullException;
import com.mybank.account.model.Client;
import com.mybank.account.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
        //Given
        Client client = null;

        // When Then
        assertThrows(ClientCannotBeNullException.class,
                () -> clientService.createClient(client));
    }
}
