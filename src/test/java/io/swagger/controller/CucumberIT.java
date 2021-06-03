package io.swagger.controller;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features/aaaa_users.feature", "src/test/resources/features/accounts.feature"},
        glue = "io.swagger.IT.steps",
        plugin = "pretty"
)

public class CucumberIT {
}
