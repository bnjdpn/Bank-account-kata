package com.bank.account.service;

import com.bank.account.model.Client;
import com.bank.account.repository.ClientRepository;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import static com.bank.account.util.ValidatorUtil.validate;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.notEmpty;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    @Inject
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = requireNonNull(clientRepository);
    }

    public Client createClient(Client client) {
        return clientRepository.save(validate(client));
    }

    public Client findClient(String id) {
        return clientRepository.findOne(notEmpty(id));
    }

}
