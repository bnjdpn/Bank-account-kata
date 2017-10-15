package com.bank.account.service;

import com.bank.account.BaseIntegrationTest;
import com.bank.account.model.Account;
import com.bank.account.model.Client;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountServiceIntegrationTest extends BaseIntegrationTest {

    private static final String CLIENT_ID = UUID.randomUUID().toString();

    @Inject
    private AccountService accountService;

    @Before
    public void before() {

        Client
            client =
            Client.builder().id(CLIENT_ID).firstname("Malfurion").lastname("Stormrage").build();

        mongoTemplate.save(client);
    }

    @Test
    public void testCreateAndFind() {
        // Given
        Account account1 = Account.builder().clientId(CLIENT_ID).name("Account 1").build();
        Account account2 = Account.builder().clientId(CLIENT_ID).name("Account 2").build();

        // When
        Account accountCreated1 = accountService.createAccount(account1);
        Account accountCreated2 = accountService.createAccount(account2);
        List<Account> accounts = accountService.findAccountsByClient(CLIENT_ID);

        // Then
        assertAccount(accountCreated1, account1);
        assertAccount(accountCreated2, account2);
        assertThat(accounts).containsExactlyInAnyOrder(accountCreated1, accountCreated2);
    }

    private void assertAccount(Account actual, Account expected) {
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getClientId()).isEqualTo(expected.getClientId());
        assertThat(actual.getAmount()).isEqualTo(expected.getAmount());
        assertThat(actual.isAllowNegativeAmount()).isEqualTo(expected.isAllowNegativeAmount());
        assertThat(actual.getCurrency()).isEqualTo(expected.getCurrency());
    }

}
