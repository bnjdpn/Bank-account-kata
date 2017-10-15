package com.bank.account.service;

import com.bank.account.exception.UnauthorizedOperationException;
import com.bank.account.model.Account;
import com.bank.account.model.Operation;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.OperationRepository;
import java.time.Instant;
import java.util.Currency;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import static com.bank.account.util.CurrencyUtil.convertAmount;
import static com.bank.account.util.ValidatorUtil.validate;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.notEmpty;

@Service
public class OperationService {

    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    @Inject
    public OperationService(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = requireNonNull(accountRepository);
        this.operationRepository = requireNonNull(operationRepository);
    }

    public List<Operation> findOperations(String accountId,
                                          Instant startOperationDate,
                                          Instant endOperationDate) {

        return operationRepository
            .findOperationsByAccountIdAndDateBetweenOrderByDateDesc(notEmpty(accountId),
                requireNonNull(startOperationDate),
                requireNonNull(endOperationDate));
    }

    public void saveOperation(Operation operation) {
        validate(operation);

        Account account = getAccount(operation);

        double newPotentialAccountAmount = getNewPotentialAccountAmount(account, operation);

        if (!isOperationAllowed(account, newPotentialAccountAmount)) {
            throw new UnauthorizedOperationException(account, operation);
        }

        account.setAmount(newPotentialAccountAmount);

        accountRepository.save(account);
        operationRepository.save(requireNonNull(operation));
    }

    private Account getAccount(Operation operation) {
        return requireNonNull(accountRepository.findOne(operation.getAccountId()));
    }

    private double getNewPotentialAccountAmount(Account account, Operation operation) {

        double accountAmount = account.getAmount();
        double operationAmount = operation.getAmount();

        Currency accountCurrency = account.getCurrency();
        Currency operationCurrency = operation.getCurrency();

        double
            operationAmountWithAccountCurrency =
            convertAmount(operationAmount, operationCurrency, accountCurrency);

        return accountAmount + operationAmountWithAccountCurrency;
    }

    private boolean isOperationAllowed(Account account, double newPotentialAccountAmount) {
        return account.isAllowNegativeAmount() || newPotentialAccountAmount >= 0;
    }
}
