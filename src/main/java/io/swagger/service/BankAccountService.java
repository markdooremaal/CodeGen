package io.swagger.service;

import io.swagger.helper.IbanGenerator;
import io.swagger.model.ArrayOfBankAccounts;
import io.swagger.model.BankAccount;
import io.swagger.repository.IBankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    private final Double BALANCE = 0.0;

    @Autowired
    IBankAccountRepository bankAccountRepository;

    @Autowired
    IbanGenerator ibanGenerator;

    public ArrayOfBankAccounts getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    public BankAccount getBankAccountByIban(String iban) {
        return bankAccountRepository.findById(iban).orElse(null);
    }

    public ArrayOfBankAccounts getBankAccountByUserId(Integer userId) {return bankAccountRepository.findByUserId(userId); }

    public void storeBankAccount(BankAccount bankAccount) {
        if (bankAccount.getBalance() == null)
            bankAccount.setBalance(BALANCE);

        if (bankAccount.getIban() == null)
            bankAccount.setIban(ibanGenerator.generate());

        bankAccountRepository.save(bankAccount);
    }

    public void updateBankAccount(BankAccount bankAccount) {
        bankAccountRepository.save(bankAccount);
    }
}
