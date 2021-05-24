package io.swagger;

import io.swagger.model.Transaction;
import io.swagger.model.User;
import io.swagger.model.enums.Role;
import io.swagger.model.enums.Status;
import io.swagger.security.UserService;
import io.swagger.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
@ComponentScan(basePackages = { "io.swagger", "io.swagger.api" , "io.swagger.configuration"})
public class Swagger2SpringBoot implements CommandLineRunner {

    @Autowired
    UserService userService;
    @Autowired
    TransactionService transactionService;

    @Override
    public void run(String... arg0) throws Exception {
        if (arg0.length > 0 && arg0[0].equals("exitcode")) {
            throw new ExitException();
        }

        System.out.println("Running MyAppRunner");

        User user = new User();
        user.setEmail("bram@bramsierhuis.nl");
        user.setPassword(("test"));
        user.setDayLimit(10.00);
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.CUSTOMER);
        user.setLastName("Sierhuis");
        user.setFirstName("Bram");
        user.setTransactionLimit(10.00);
        userService.add(user);

        Transaction transaction = new Transaction();
        transaction.setAccountFrom("nl58ingb1122832273");
        transaction.setAccountTo("nl05ingb9732254661");
        transaction.userPerforming(user.getId());
        transaction.setAmount(500.00);
        transactionService.storeTransaction(transaction);

        Transaction transaction2 = new Transaction();
        transaction2.setAccountFrom("nl58ingb1122832273");
        transaction2.setAccountTo("nl05rabo9732254661");
        transaction2.userPerforming(user.getId());
        transaction2.setAmount(420.00);
        transactionService.storeTransaction(transaction2);
    }

    public static void main(String[] args) throws Exception {
        new SpringApplication(Swagger2SpringBoot.class).run(args);
    }

    class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }
}
