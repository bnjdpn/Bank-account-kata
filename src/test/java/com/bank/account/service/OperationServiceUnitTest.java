package com.bank.account.service;

import com.bank.account.exception.UnauthorizedOperationException;
import com.bank.account.model.Account;
import com.bank.account.model.Operation;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.OperationRepository;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class OperationServiceUnitTest {

    @Mock
    private AccountRepository accountRepositoryMock;

    @Mock
    private OperationRepository operationRepositoryMock;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    private OperationService operationService;

    @Before
    public void before() {
        initMocks(this);
        operationService = new OperationService(accountRepositoryMock, operationRepositoryMock);
    }

    @Test
    public void testSaveDepositOperation() {
        // Given
        Account
            account =
            Account.builder().id(UUID.randomUUID().toString()).name("My account").amount(100)
                .build();

        Operation
            operation =
            Operation.builder().accountId(account.getId()).label("Stormwind Auction House")
                .amount(100).build();

        when(accountRepositoryMock.findOne(account.getId())).thenReturn(account);

        // When
        operationService.saveOperation(operation);

        // Then
        verify(operationRepositoryMock).save(operation);
        verify(accountRepositoryMock).save(accountCaptor.capture());

        Account accountCaptured = accountCaptor.getValue();
        assertThat(accountCaptured.getId()).isEqualTo(account.getId());
        assertThat(accountCaptured.getAmount()).isEqualTo(200);
    }

    @Test
    public void testSaveWithdrawalOperation() {
        // Given
        Account
            account =
            Account.builder().id(UUID.randomUUID().toString()).name("My account").amount(100)
                .build();

        Operation
            operation =
            Operation.builder().accountId(account.getId()).label("Auto-Hammer").amount(-200)
                .build();

        when(accountRepositoryMock.findOne(account.getId())).thenReturn(account);

        // When
        operationService.saveOperation(operation);

        // Then
        verify(operationRepositoryMock).save(operation);
        verify(accountRepositoryMock).save(accountCaptor.capture());

        Account accountCaptured = accountCaptor.getValue();
        assertThat(accountCaptured.getId()).isEqualTo(account.getId());
        assertThat(accountCaptured.getAmount()).isEqualTo(-100);
    }

    @Test
    public void testSaveWithdrawalOperation_withNegativeAmountUnallowed() {
        // Given
        Account
            account =
            Account.builder().id(UUID.randomUUID().toString()).allowNegativeAmount(false)
                .name("My account").amount(100).build();

        Operation
            operation =
            Operation.builder().accountId(account.getId()).label("Merchant Cheng").amount(-200)
                .build();

        when(accountRepositoryMock.findOne(account.getId())).thenReturn(account);

        // Then
        assertThatThrownBy(() -> operationService.saveOperation(operation))
            .isInstanceOf(UnauthorizedOperationException.class);

        verify(operationRepositoryMock, never()).save(operation);
        verify(accountRepositoryMock, never()).save(accountCaptor.capture());
    }

}
