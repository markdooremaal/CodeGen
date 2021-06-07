package io.swagger.helper;

import io.cucumber.java.bs.A;
import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.Transfer;
import io.swagger.model.User;
import io.swagger.model.enums.AccountType;
import io.swagger.model.enums.Role;
import io.swagger.model.enums.Status;
import io.swagger.model.enums.Type;
import io.swagger.service.BankAccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.TransferService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//Class to add default data to the h2 database
@Service
public class DatabaseSeeder {

    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    TransferService transferService;

    @Autowired
    BankAccountService bankAccountService;

    //Add default data to the database
    public void seedDatabase(){
        //Add users
        User bram = new User();
        bram.setEmail("bram@bramsierhuis.nl");
        bram.setPassword(("test"));
        bram.setDayLimit(1000.00);
        bram.setStatus(Status.ACTIVE);
        bram.setRole(Role.CUSTOMER);
        bram.setLastName("Sierhuis");
        bram.setFirstName("Bram");
        bram.setTransactionLimit(100.00);
        userService.add(bram);

        User mark = new User();
        mark.setEmail("mark@gmail.com");
        mark.setPassword(("test"));
        mark.setDayLimit(1000.00);
        mark.setStatus(Status.ACTIVE);
        mark.setRole(Role.EMPLOYEE);
        mark.setLastName("van Dooremaal");
        mark.setFirstName("Mark");
        mark.setTransactionLimit(100.00);
        userService.add(mark);

        User employee = new User();
        employee.setEmail("employee@bank.nl");
        employee.setPassword(("test"));
        employee.setDayLimit(1000.00);
        employee.setStatus(Status.ACTIVE);
        employee.setRole(Role.EMPLOYEE);
        employee.setLastName("Jake");
        employee.setFirstName("the Snake");
        employee.setTransactionLimit(100.00);
        userService.add(employee);

        User bank = new User();
        bank.setEmail("info@inhollandbank.com");
        bank.setPassword("39282y7whorfznffuailfw8hf23AHDS*(A(Hf98eh");
        bank.setFirstName("Bank of");
        bank.setLastName("Inholland");
        userService.add(bank);

        //Add bankaccounts
        BankAccount mainAccount = new BankAccount();
        mainAccount.setIban("nl01inho0000000001");
        mainAccount.setUserId(bank.getId());
        mainAccount.setStatus(Status.ACTIVE);
        mainAccount.setAccountType(AccountType.REGULAR);
        mainAccount.balance(1000000000.0);
        mainAccount.absoluteLimit(0.0);
        bankAccountService.storeBankAccount(mainAccount);

        BankAccount bramRegular = new BankAccount();
        bramRegular.setIban("nl01inho1100000001");
        bramRegular.setUserId(bram.getId());
        bramRegular.setStatus(Status.ACTIVE);
        bramRegular.setAccountType(AccountType.REGULAR);
        bramRegular.balance(20.0);
        bramRegular.absoluteLimit(-1000.0);
        bankAccountService.storeBankAccount(bramRegular);

        BankAccount bramSavings = new BankAccount();
        bramSavings.setIban("nl01inho2200000001");
        bramSavings.setUserId(bram.getId());
        bramSavings.setStatus(Status.ACTIVE);
        bramSavings.setAccountType(AccountType.SAVINGS);
        bramSavings.balance(20.0);
        bramSavings.absoluteLimit(0.0);
        bankAccountService.storeBankAccount(bramSavings);

        BankAccount bramInactive = new BankAccount();
        bramInactive.setIban("nl01inho5500000001");
        bramInactive.setUserId(bram.getId());
        bramInactive.setStatus(Status.INACTIVE);
        bramInactive.setAccountType(AccountType.REGULAR);
        bramInactive.balance(20.0);
        bramInactive.absoluteLimit(0.0);
        bankAccountService.storeBankAccount(bramInactive);

        BankAccount markRegular = new BankAccount();
        markRegular.setIban("nl01inho3300000001");
        markRegular.setUserId(mark.getId());
        markRegular.setStatus(Status.ACTIVE);
        markRegular.setAccountType(AccountType.REGULAR);
        markRegular.balance(20.0);
        markRegular.absoluteLimit(-100.0);
        bankAccountService.storeBankAccount(markRegular);

        BankAccount markSavings = new BankAccount();
        markSavings.setIban("nl01inho4400000001");
        markSavings.setUserId(mark.getId());
        markSavings.setStatus(Status.ACTIVE);
        markSavings.setAccountType(AccountType.SAVINGS);
        markSavings.balance(20.0);
        markSavings.absoluteLimit(-100.0);
        bankAccountService.storeBankAccount(markSavings);

        bram.addBankAccountsItem(bramRegular);
        bram.addBankAccountsItem(bramSavings);
        bram.addBankAccountsItem(bramInactive);
        userService.update(bram);

        mark.addBankAccountsItem(markRegular);
        mark.addBankAccountsItem(markSavings);
        userService.update(mark);

        bank.addBankAccountsItem(mainAccount);
        userService.update(bank);

        //Add transactions and transfers
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
