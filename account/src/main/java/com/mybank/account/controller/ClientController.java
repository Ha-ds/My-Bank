package com.mybank.account.controller;

import com.mybank.account.dto.ClientDto;
import com.mybank.account.model.Client;
import com.mybank.account.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/create-client")
   public void createClient(@RequestBody Client client) {
        clientService.createClient(client);
    }

    @GetMapping("/get-client/{iban}")
    public ClientDto getClientByIban(@PathVariable String iban) {
      return clientService.getClientByIban(iban);
    }

    @PutMapping("/update-client")
    public void updateClient(@RequestBody ClientDto clientDto) {
        clientService.updateClient(clientDto);
    }
}
