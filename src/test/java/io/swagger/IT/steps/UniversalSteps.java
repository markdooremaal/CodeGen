package io.swagger.IT.steps;

import io.cucumber.java.en.Then;
import io.swagger.model.StateSingleton;
import org.junit.Assert;

//Class with Steps that are used in multiple features
//The error or response is stored in the singleton so they can be accessed from here
public class UniversalSteps{
    @Then("Krijg ik een error {int}")
    public void krijgIkEenError(int statusCode) {
        Assert.assertEquals(statusCode, StateSingleton.getInstance().getHttpClientErrorException().getRawStatusCode());
    }

    @Then("Is de status van het request {int}")
    public void isDeStatusVanHetRequest(int expected) {
        int response = StateSingleton.getInstance().getResponseEntity().getStatusCodeValue();
        Assert.assertEquals(expected, response);
    }
}
