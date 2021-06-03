package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.swagger.model.BankAccount;
import io.swagger.model.StateSingleton;
import io.swagger.model.enums.AccountType;
import io.swagger.model.enums.Status;
import org.junit.Assert;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//Class that contains the Step Definitions to test the BankAccount endpoints
public class AccountSteps extends Base {

    @When("Ik alle accounts ophaal")
    public void ikAlleAccountsOphaal() throws URISyntaxException {
        //URI to send the request to
        URI uri = new URI(baseUrl + "bankaccounts");

        //Add token to the request
        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());

        //Create entitiy with headers and send the request, store the response
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.GET, entity, String.class));
    }

    @When("Ik een nieuw accout aanmaak")
    public void ikEenNieuwAccountAanmaak() throws URISyntaxException, JsonProcessingException, HttpClientErrorException, IOException {
        //URI to send the request to
        URI uri = new URI(baseUrl + "bankaccounts");

        //Object to add
        BankAccount account = new BankAccount();
        account.setAccountType(AccountType.SAVINGS);
        account.setStatus(Status.INACTIVE);
        account.setUserId(USER_ID);
        account.setAbsoluteLimit(1000.0);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(employee_jwt);

        try{
            //Send the request, store results
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(account), headers);
            ResponseEntity<String> responseEntity = template.postForEntity(uri, entity, String.class);
            StateSingleton.getInstance().setResponseEntity(responseEntity);

            //Store the created iban for future usage
            BankAccount createdAccount = objectMapper.readValue(responseEntity.getBody(), BankAccount.class);
            StateSingleton.getInstance().setCreatedIban(createdAccount.getIban());
        } catch (HttpClientErrorException ex){
            StateSingleton.getInstance().setHttpClientErrorException(ex);
        }
    }

    @When("Ik een bestaand account inactief maak")
    public void ikEenBestaandAccountInactiefMaak() throws URISyntaxException {
        //URI to send the request to
        URI uri = new URI(baseUrl + "bankaccount/" + StateSingleton.getInstance().getCreatedIban());

        //Add headers
        headers.setBearerAuth(employee_jwt);

        //Send the request and store the results
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.DELETE, entity, String.class));
    }

    @When("Ik een bestaand en inactief account ophaal")
    public void ikEenBestaandInactiefAccountOphaal() throws URISyntaxException{
        //URI to send the request to
        URI uri = new URI(baseUrl + "bankaccount/" + StateSingleton.getInstance().getCreatedIban());

        //Add headers
        headers.setBearerAuth(employee_jwt);

        //Send the request and store the results
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.GET, entity, String.class));
    }

    @And("Is de status van het account inactive")
    public void isDeStatusVanHetAccountInactive() throws IOException {
        //Test if the received account is inactive
        BankAccount account = objectMapper.readValue(StateSingleton.getInstance().getResponseEntity().getBody(), BankAccount.class);
        Assert.assertEquals(Status.INACTIVE, account.getStatus());
    }

    @When("Ik een bestaand account update")
    public void ikEenBestaandAccountUpdate() throws URISyntaxException, JsonProcessingException { //@TODO: Standard account object
        //The URI for the request
        URI uri = new URI(baseUrl + "bankaccount/" + StateSingleton.getInstance().getCreatedIban());

        //Object to send
        BankAccount account = new BankAccount();
        account.setAccountType(AccountType.REGULAR);
        account.setStatus(Status.ACTIVE);
        account.setUserId(USER_ID);
        account.setBalance(100.0);
        account.setAbsoluteLimit(100.0);

        //Set headers
        headers.setBearerAuth(employee_jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Send the request and store the results
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(account), headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.PUT, entity, String.class));
    }

    @When("Ik een niet bestaand account update")
    public void ikEenNietBestaandAccountUpdate() throws URISyntaxException, JsonProcessingException {
        //The URI for the request
        URI uri = new URI(baseUrl + "bankaccount/" + UNUSED_IBAN);

        //Object to send
        BankAccount account = new BankAccount();
        account.setAccountType(AccountType.REGULAR);
        account.setStatus(Status.ACTIVE);
        account.setUserId(USER_ID);

        //Set headers
        headers.setBearerAuth(employee_jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try{
            //Send the request and store the results
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(account), headers);
            StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.PUT, entity, String.class));
        } catch (HttpClientErrorException ex){
            StateSingleton.getInstance().setHttpClientErrorException(ex);
        }
    }
}