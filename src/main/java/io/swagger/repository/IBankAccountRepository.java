package io.swagger.repository;

import io.swagger.model.ArrayOfBankAccounts;
import io.swagger.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBankAccountRepository extends JpaRepository<BankAccount, String> {
    ArrayOfBankAccounts findAll();

    BankAccount findByIbanLike(String iban);
}