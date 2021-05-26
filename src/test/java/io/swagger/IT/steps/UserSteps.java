package io.swagger.IT.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class UserSteps {

    private HttpHeaders headers = new HttpHeaders();
    private String baseUrl = "http://localhost:8080/";
    private RestTemplate template = new RestTemplate();
    private ResponseEntity<String> responseEntity;

    @When("Ik alle users ophaal")
    public void ikAlleUsersOphaal() throws URISyntaxException {
        URI uri = new URI(baseUrl + "users");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.getForEntity(uri, String.class);
    }

    @Then("Is de status van het request {int}")
    public void isDeStatusVanHetRequest(int expected) {
        int response = responseEntity.getStatusCodeValue();
        Assert.assertEquals(expected, response);
    }
}
