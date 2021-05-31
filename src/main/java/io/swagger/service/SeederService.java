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
        userService.add(bram);

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
        transaction.userPerforming(mark.getId());
        transaction.setAmount(500.00);
        transactionService.storeTransaction(transaction);

        Transaction transaction2 = new Transaction();
        transaction2.setAccountFrom("nl58ingb1122832273");
        transaction2.setAccountTo("nl05rabo9732254661");
        transaction2.userPerforming(mark.getId());
        transaction2.setAmount(420.00);
        transactionService.storeTransaction(transaction2);

        Transfer transfer = new Transfer();
        transfer.setAccount("nl58ingb1122832273");
        transfer.setType(Type.DEPOSIT);
        transfer.setAmount(30.56);
        transfer.setUserPerforming(mark.getId());
        transferService.storeTransfer(transfer);
    }
}
