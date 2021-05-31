package io.swagger.service;

import io.swagger.model.ArrayOfTransactions;
import io.swagger.model.Transaction;
import io.swagger.repository.ITransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private ITransactionRepository transactionRepository;

    public ArrayOfTransactions getAllTransactions() {
        return transactionRepository.findAll();
    }

    public void storeTransaction(Transaction transaction) {
        OffsetDateTime dateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        transaction.setTimestamp(dateTime);
        transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Integer id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public ArrayOfTransactions getTransactionsForUser(String iban){
        return transactionRepository.findByAccountFromLikeOrAccountToLike(iban, iban);
    }
}
