package com.bank.account.repository;

import com.bank.account.model.Operation;
import java.time.Instant;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OperationRepository extends MongoRepository<Operation, String> {

    List<Operation> findOperationsByAccountIdAndDateBetweenOrderByDateDesc(String accountId,
                                                                           Instant start,
                                                                           Instant end);
}
