package io.swagger.service;

import io.cucumber.java.bs.A;
import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.Transfer;
import io.swagger.model.User;
import io.swagger.model.enums.AccountType;
import io.swagger.model.enums.Role;
import io.swagger.model.enums.Status;
import io.swagger.model.enums.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//Class to add default data to the h2 database
@Service
public class SeederService {

    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    TransferService transferService;

    @Autowired
    BankAccountService bankAccountService;

    public void seedDatabase(){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setIban("nl58ingb1122832273");
        bankAccount.setBalance(0.0);
        bankAccount.setStatus(Status.ACTIVE);
        bankAccount.setAccountType(AccountType.REGULAR); //@TODO: Proper type
        bankAccount.setAbsoluteLimit(-100.0);
        bankAccount.setUserId(2);
        bankAccountService.storeBankAccount(bankAccount);

        BankAccount bankAccount2 = new BankAccount();
        bankAccount2.setIban("nl58ingb1111832211");
        bankAccount2.setBalance(20000.0);
        bankAccount2.setStatus(Status.ACTIVE);
        bankAccount2.setAccountType(AccountType.REGULAR); //@TODO: Proper type
        bankAccount2.setAbsoluteLimit(0.0);
        bankAccount2.setUserId(2);
        bankAccountService.storeBankAccount(bankAccount2);

        User bram = new User();
        bram.setEmail("bram@bramsierhuis.nl");
        bram.setPassword(("test"));
        bram.setDayLimit(10.00);
        bram.setStatus(Status.ACTIVE);
        bram.setRole(Role.CUSTOMER);
        bram.setLastName("Sierhuis");
        bram.setFirstName("Bram");
        bram.setTransactionLimit(10.00);
        bram.addBankAccountsItem(bankAccount);
        bram.addBankAccountsItem(bankAccount2);
        userService.add(bram);

        BankAccount bankAccount3 = new BankAccount();
        bankAccount3.setIban("nl58rabo2424242424");
        bankAccount3.setBalance(20000.0);
        bankAccount3.setStatus(Status.ACTIVE);
        bankAccount3.setAccountType(AccountType.REGULAR); //@TODO: Proper type
        bankAccount3.setAbsoluteLimit(0.0);
        bankAccount3.setUserId(2);
        bankAccountService.storeBankAccount(bankAccount3);

        User mark = new User();
        mark.setEmail("mark@gmail.com");
        mark.setPassword(("test"));
        mark.setDayLimit(10.00);
        mark.setStatus(Status.ACTIVE);
        mark.setRole(Role.EMPLOYEE);
        mark.setLastName("van Dooremaal");
        mark.setFirstName("Mark");
        mark.setTransactionLimit(10.00);
        userService.add(mark);

        Transaction transaction = new Transaction();
        transaction.setAccountFrom("nl58ingb1122832273");
        transaction.setAccountTo("nl05ingb9732254661");
        transaction.userPerforming(bram.getId());
        transaction.setAmount(500.00);
        transactionService.storeTransaction(transaction);

        Transaction transaction2 = new Transaction();
        transaction2.setAccountFrom("nl58ingb1122832273");
        transaction2.setAccountTo("nl58ingb1111832211");
        transaction2.userPerforming(bram.getId());
        transaction2.setAmount(420.00);
        transactionService.storeTransaction(transaction2);

        Transaction transaction3 = new Transaction();
        transaction3.setAccountFrom("nl58rabo2424242424");
        transaction3.setAccountTo("nl58rabo2424240000");
        transaction3.userPerforming(mark.getId());
        transaction3.setAmount(500.00);
        transactionService.storeTransaction(transaction3);

        Transfer transfer = new Transfer();
        transfer.setAccount("nl58ingb1122832273");
        transfer.setType(Type.DEPOSIT);
        transfer.setAmount(30.56);
        transfer.setUserPerforming(mark.getId());
        transferService.storeTransfer(transfer);
    }
}
