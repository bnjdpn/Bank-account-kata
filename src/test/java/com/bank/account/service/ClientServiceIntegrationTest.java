package com.bank.account.service;

import com.bank.account.BaseIntegrationTest;
import com.bank.account.model.Client;
import javax.inject.Inject;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ClientServiceIntegrationTest extends BaseIntegrationTest {

    @Inject
    private ClientService clientService;

    @Test
    public void testCreateAndFind() {
        // Given
        Client client = Client.builder().firstname("Anduin").lastname("Wrynn").build();

        // When
        Client clientCreated = clientService.createClient(client);
        Client clientFound = clientService.findClient(clientCreated.getId());

        // Then
        assertThat(clientCreated.getId()).isNotEmpty();
        assertThat(clientCreated.getFirstname()).isEqualTo(client.getFirstname());
        assertThat(clientCreated.getLastname()).isEqualTo(client.getLastname());
        assertThat(clientFound).isEqualTo(clientCreated);
    }

}
