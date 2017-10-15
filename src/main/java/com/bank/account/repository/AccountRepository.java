package com.bank.account.repository;

import com.bank.account.model.Account;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, String> {

    List<Account> findAccountsByClientId(String clientId);

}
