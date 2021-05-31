package io.swagger.service;

import io.swagger.model.ArrayOfBankAccounts;
import io.swagger.model.BankAccount;
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

    public BankAccount getBankAccountByIban(String iban) {
        return bankAccountRepository.findById(iban).orElse(null);
    }

    public void storeBankAccount(BankAccount bankAccount) {
        //:TODO Iban willekeurig op basis van template?
        bankAccountRepository.save(bankAccount);
    }
}
