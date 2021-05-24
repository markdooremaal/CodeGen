package io.swagger.controller;

import io.swagger.model.User;
import io.swagger.model.Transaction;
import io.swagger.model.enums.Role;
import io.swagger.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * Return a list of all transactions
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> index(@RequestParam Map<String, String> params) {

        List<io.swagger.model.Transaction> transactions = new ArrayList<>();

        User user = new User(); // :TODO Should be based on JWT.
        user.setRole(Role.EMPLOYEE);

        if (user.getRole() == Role.EMPLOYEE) {
            transactions = transactionService.getAllTransactions();
        } else if (user.getRole() == Role.CUSTOMER) {
            transactions = transactionService.getAllTransactions();
            // Maybe use ibanFromOrTo
            // Get all ibans for specific user
            // Loop through results filter on iban- from/to add matching to list.
        }

        /*
         * Filter based on IBAN-From, IBAN-To or both
         */
        if (params.containsKey("ibanFrom") && !params.containsKey("ibanTo")) {
            transactions = transactions.stream().filter(
                    t -> params.get("ibanFrom").equals(t.getAccountFrom())).collect(Collectors.toList()
            );
        } else if (!params.containsKey("ibanFrom") && params.containsKey("ibanTo")) {
            transactions = transactions.stream().filter(
                    t -> params.get("ibanTo").equals(t.getAccountTo())).collect(Collectors.toList()
            );
        } else if (params.containsKey("ibanFrom") && params.containsKey("ibanTo")) {
            transactions = transactions.stream().filter(
                    t -> params.get("ibanFrom")
                            .equals(t.getAccountFrom())
                            && params.get("ibanTo").equals(t.getAccountTo())).collect(Collectors.toList());
        }

        /*
         * Filter based on (Offset)DateTime
         */
        if (params.containsKey("timestamp")) {
            transactions = transactions.stream().filter(
                    t -> OffsetDateTime.parse(params.get("timestamp")).equals(t.getTimestamp())).collect(Collectors.toList()
            );
        }

        /*
         * Only show transactions where the IBAN-From or IBAN-To matches query
         * Only available if user is an employee
         */
        if (params.containsKey("ibanToOrFrom") && user.getRole() == Role.EMPLOYEE) {
            transactions = transactions.stream().filter(
                    t -> params.get("ibanToOrFrom").equals(t.getAccountFrom())
                            || params.get("ibanToOrFrom").equals(t.getAccountTo())).collect(Collectors.toList()
            );
        }

        /*
         * Filter based on the user performing the transaction
         * Only available if user is an employee
         */
        if (params.containsKey("userPerforming") && user.getRole() == Role.EMPLOYEE) {
            Integer userPerforming = Integer.parseInt(params.get("userPerforming"));
            transactions = transactions.stream().filter(
                    t -> userPerforming.equals(t.getUserPerforming())).collect(Collectors.toList()
            );
        }

        return ResponseEntity.status(200).body(transactions);
    }

    /**
     * Store a newly created transaction in storage.
     */
    @PostMapping("/transactions")
    public ResponseEntity<Transaction> store(@RequestBody Transaction request) {
        User user = new User(); // :TODO Should be based on JWT.

        Transaction transaction = new Transaction();
        transaction.setAccountFrom(request.getAccountFrom()); // :TODO Check if accounts exists. Also need a function to get account object based on iban
        transaction.setAccountTo(request.getAccountTo());
        transaction.userPerforming(user.getId());
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
