package com.bank.account.exception;

import com.bank.account.model.Account;
import com.bank.account.model.Operation;

public class UnauthorizedOperationException extends RuntimeException {

    private static final String MESSAGE = "Unauthorized operation %s on account %s";

    public UnauthorizedOperationException(Account account, Operation operation) {
        super(String.format(MESSAGE, operation, account.getId()));
    }

}
