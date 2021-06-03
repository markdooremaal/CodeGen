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
        User bram = new User();
        bram.setEmail("bram@bramsierhuis.nl");
        bram.setPassword(("test"));
        bram.setDayLimit(20.00);
        bram.setStatus(Status.ACTIVE);
        bram.setRole(Role.CUSTOMER);
        bram.setLastName("Sierhuis");
        bram.setFirstName("Bram");
        bram.setTransactionLimit(10.00);
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

        User employee = new User();
        employee.setEmail("employee@bank.nl");
        employee.setPassword(("test"));
        employee.setDayLimit(10.00);
        employee.setStatus(Status.ACTIVE);
        employee.setRole(Role.EMPLOYEE);
        employee.setLastName("Jake");
        employee.setFirstName("the Snake");
        employee.setTransactionLimit(10.00);
        userService.add(employee);

        User bank = new User();
        bank.setEmail("info@inhollandbank.com");
        bank.setPassword("39282y7whorfznffuailfw8hf23AHDS*(A(Hf98eh");
        bank.setFirstName("Bank of");
        bank.setLastName("Inholland");
        userService.add(bank);

        BankAccount mainAccount = new BankAccount();
        mainAccount.setIban("nl01inho0000000001");
        mainAccount.setUserId(bank.getId());
        mainAccount.setStatus(Status.ACTIVE);
        mainAccount.setAccountType(AccountType.REGULAR);
        mainAccount.balance(1000000000.0);
        mainAccount.absoluteLimit(0.0);
        bankAccountService.storeBankAccount(mainAccount);

        BankAccount bramRegular = new BankAccount();
        bramRegular.setUserId(bram.getId());
        bramRegular.setStatus(Status.ACTIVE);
        bramRegular.setAccountType(AccountType.REGULAR);
        bramRegular.balance(500.0);
        bramRegular.absoluteLimit(-1000.0);
        bankAccountService.storeBankAccount(bramRegular);

        BankAccount bramSavings = new BankAccount();
        bramSavings.setUserId(bram.getId());
        bramSavings.setStatus(Status.ACTIVE);
        bramSavings.setAccountType(AccountType.SAVINGS);
        bramSavings.balance(12000.0);
        bramSavings.absoluteLimit(0.0);
        bankAccountService.storeBankAccount(bramSavings);

        BankAccount markRegular = new BankAccount();
        markRegular.setUserId(mark.getId());
        markRegular.setStatus(Status.ACTIVE);
        markRegular.setAccountType(AccountType.REGULAR);
        markRegular.balance(320.0);
        markRegular.absoluteLimit(-100.0);
        bankAccountService.storeBankAccount(markRegular);

        bram.addBankAccountsItem(bramRegular);
        bram.addBankAccountsItem(bramSavings);
        userService.update(bram);

        mark.addBankAccountsItem(markRegular);
        userService.update(mark);

        bank.addBankAccountsItem(mainAccount);
        userService.update(bank);

        Transaction fromBramSavingsToBramRegular = new Transaction();
        fromBramSavingsToBramRegular.setAccountFrom(bramRegular.getIban());
        fromBramSavingsToBramRegular.setAccountTo(bramSavings.getIban());
        fromBramSavingsToBramRegular.userPerforming(bram.getId());
        fromBramSavingsToBramRegular.setAmount(100.0);
        transactionService.storeTransaction(fromBramSavingsToBramRegular);

        Transaction fromBramToMark = new Transaction();
        fromBramToMark.setAccountFrom(bramRegular.getIban());
        fromBramToMark.setAccountTo(markRegular.getIban());
        fromBramToMark.userPerforming(bram.getId());
        fromBramToMark.setAmount(150.0);
        transactionService.storeTransaction(fromBramToMark);

        Transfer bramDeposit = new Transfer();
        bramDeposit.setAccount(bramRegular.getIban());
        bramDeposit.setType(Type.DEPOSIT);
        bramDeposit.setAmount(20.0);
        bramDeposit.setUserPerforming(bram.getId());
        transferService.storeTransfer(bramDeposit);

        Transfer markWithdrawal = new Transfer();
        markWithdrawal.setAccount(markRegular.getIban());
        markWithdrawal.setType(Type.WITHDRAWAL);
        markWithdrawal.setAmount(20.0);
        markWithdrawal.setUserPerforming(mark.getId());
        transferService.storeTransfer(markWithdrawal);


    }
}
