package io.swagger.repository;

import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByAccountFromLike(String accountFrom);

    List<Transaction> findByAccountToLike(String accountTo);

    List<Transaction> findByAccountFromLikeAndAccountToLike(String accountFrom, String accountTo);

}
