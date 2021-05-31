package io.swagger.service;

import io.swagger.model.ArrayOfBankAccounts;
import io.swagger.repository.IBankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    @Autowired
    IBankAccountRepository bankAccountRepository;

    public ArrayOfBankAccounts getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }
}
