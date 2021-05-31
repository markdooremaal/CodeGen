package io.swagger.repository;

import io.swagger.model.ArrayOfTransactions;
import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITransactionRepository extends JpaRepository<Transaction, Integer> {
    ArrayOfTransactions findAll();

    ArrayOfTransactions findByAccountFromLike(String accountFrom);

    ArrayOfTransactions findByAccountToLike(String accountTo);

    ArrayOfTransactions findByAccountFromLikeAndAccountToLike(String accountFrom, String accountTo);

    ArrayOfTransactions findByAccountFromLikeOrAccountToLike(String accountFrom, String accountTo);

}
