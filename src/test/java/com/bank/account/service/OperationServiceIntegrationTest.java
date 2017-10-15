package com.bank.account.service;

import com.bank.account.BaseIntegrationTest;
import com.bank.account.exception.UnauthorizedOperationException;
import com.bank.account.model.Account;
import com.bank.account.model.Client;
import com.bank.account.model.Operation;
import com.bank.account.repository.AccountRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OperationServiceIntegrationTest extends BaseIntegrationTest {

    private static final String CLIENT_ID = UUID.randomUUID().toString();
    private static final String ACCOUNT_ID = UUID.randomUUID().toString();

    @Inject
    private OperationService operationService;

    @Inject
    private AccountRepository accountRepository;

    @Before
    public void before() {

        Client
            client =
            Client.builder().id(CLIENT_ID).firstname("Magni").lastname("Bronzebeard").build();

        Account
            account =
            Account.builder().id(ACCOUNT_ID).name("Ironforge Bank").clientId(CLIENT_ID).build();

        mongoTemplate.save(client);
        mongoTemplate.save(account);
    }

    @Test
    public void testDepositOperation() {
        // Given
        Operation
            operation =
            Operation.builder().accountId(ACCOUNT_ID).label("Rams selling").amount(1337).build();

        // When
        operationService.saveOperation(operation);
        List<Operation>
            operations =
            operationService.findOperations(ACCOUNT_ID,
                Instant.now().minus(1, ChronoUnit.DAYS),
                Instant.now().plus(1, ChronoUnit.DAYS));

        // Then
        Account account = accountRepository.findOne(ACCOUNT_ID);
        assertThat(account.getAmount()).isEqualTo(operation.getAmount());
        assertThat(operations).containsOnly(operation);
    }

    @Test
    public void testWithdrawalOperation() {
        // Given
        Operation
            operation =
            Operation.builder().accountId(ACCOUNT_ID).label("Starlight Roses").amount(-1337)
                .build();

        // When
        operationService.saveOperation(operation);
        List<Operation>
            operations =
            operationService.findOperations(ACCOUNT_ID,
                Instant.now().minus(1, ChronoUnit.DAYS),
                Instant.now().plus(1, ChronoUnit.DAYS));

        // Then
        Account account = accountRepository.findOne(ACCOUNT_ID);
        assertThat(account.getAmount()).isEqualTo(operation.getAmount());
        assertThat(operations).containsOnly(operation);
    }

    @Test
    public void testWithdrawalOperation_withUnallowedNegativeAmount() {
        // Given
        Account
            account =
            Account.builder().id(ACCOUNT_ID).name("Ironforge Bank").allowNegativeAmount(false)
                .amount(42).clientId(CLIENT_ID).build();
        mongoTemplate.save(account);

        Operation
            operation =
            Operation.builder().accountId(ACCOUNT_ID).label("Starlight Roses").amount(-1337)
                .build();

        // When / Then
        assertThatThrownBy(() -> operationService.saveOperation(operation))
            .isInstanceOf(UnauthorizedOperationException.class).hasMessageContaining(ACCOUNT_ID);

        List<Operation>
            operations =
            operationService.findOperations(ACCOUNT_ID,
                Instant.now().minus(1, ChronoUnit.DAYS),
                Instant.now().plus(1, ChronoUnit.DAYS));

        assertThat(operations).isEmpty();
    }

    @Test
    public void testFindOperations() {
        // Given
        Operation
            todayOperation =
            Operation.builder().accountId(ACCOUNT_ID).label("Starlight Roses").amount(-1337)
                .build();
        Operation
            yesterdayOperation =
            Operation.builder().accountId(ACCOUNT_ID).date(Instant.now().minus(1, ChronoUnit.DAYS))
                .label("Roots of Shaladrassil").amount(1234).build();
        Operation
            lastWeekOperation =
            Operation.builder().accountId(ACCOUNT_ID).date(Instant.now().minus(7, ChronoUnit.DAYS))
                .label("Swift White Ram").amount(10).build();

        mongoTemplate.save(todayOperation);
        mongoTemplate.save(yesterdayOperation);
        mongoTemplate.save(lastWeekOperation);

        // When
        List<Operation>
            lastTwoDaysOperations =
            operationService
                .findOperations(ACCOUNT_ID, Instant.now().minus(2, ChronoUnit.DAYS), Instant.now());

        // Then
        assertThat(lastTwoDaysOperations).containsExactly(todayOperation, yesterdayOperation);
    }
}
