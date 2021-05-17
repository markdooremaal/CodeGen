package io.swagger;

import io.swagger.model.User;
import io.swagger.model.enums.Role;
import io.swagger.model.enums.Status;
import io.swagger.security.UserService;
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
