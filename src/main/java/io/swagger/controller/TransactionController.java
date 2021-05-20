package io.swagger.controller;

import io.swagger.model.Transaction;
import io.swagger.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * Return a list of all transactions
     */
    @GetMapping("/transactions")
    public String index(@RequestParam Optional<Integer> userId, Optional<String> ibanFrom, Optional<String> ibanTo, Optional<Integer> userPerforming, Optional<String> ibanToOrFrom) {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ibanFrom.orElseGet(() -> "not provided");
        //return ResponseEntity.status(200).body(transactions);
    }

    /**
     * Store a newly created transaction in storage.
     */
    @PostMapping("/transactions")
    public ResponseEntity<Transaction> store(@RequestBody Transaction request) {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(request.getAccountFrom()); // :TODO Check if accounts exists. Also need a function to get account object based in iban
        transaction.setAccountTo(request.getAccountTo());
        transaction.userPerforming(request.getUserPerforming()); // :TODO Should be the id based on JWT.
        transaction.setAmount(request.getAmount());

        // if amount <= user.daylimit && amount <= user.transactionLimit && amount <= accountFrom.amount
        // if !(acountFrom.amount - amount) <= acountFrom.absoluteLimit
        // accountFrom.setAmount(-amount)
        // else abort
        // accountTo.setAmount(+amount)
        transactionService.storeTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    /**
     * Display the specified transaction.
     * :TODO Check if role == employee else user needs to be sender or receiver.
     */
    @GetMapping("/transaction/{id}")
    public ResponseEntity<Transaction> show(@PathVariable Integer id) {
        try {
            Transaction transaction = transactionService.getTransactionById(id);
            return ResponseEntity.status(HttpStatus.OK).body(transaction);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
