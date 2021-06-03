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

public class AccountSteps extends Base {

    @When("Ik alle accounts ophaal")
    public void ikAlleAccountsOphaal() throws URISyntaxException {
        URI uri = new URI(baseUrl + "bankaccounts");
        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.GET, entity, String.class));
    }

    @When("Ik een nieuw accout aanmaak")
    public void ikEenNieuwAccountAanmaak() throws URISyntaxException, JsonProcessingException, HttpClientErrorException, IOException {
        URI uri = new URI(baseUrl + "bankaccounts");

        BankAccount account = new BankAccount();
        account.setAccountType(AccountType.REGULAR);
        account.setStatus(Status.INACTIVE);
        account.setUserId(USER_ID);
        account.setIban("nl31inho9492452917");
        account.setAbsoluteLimit(1000.0);

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(employee_jwt);

        try{
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(account), headers);
            ResponseEntity<String> responseEntity = template.postForEntity(uri, entity, String.class);
            StateSingleton.getInstance().setResponseEntity(responseEntity);

            BankAccount createdAccount = objectMapper.readValue(responseEntity.getBody(), BankAccount.class);
            StateSingleton.getInstance().setCreated_iban(createdAccount.getIban());
        } catch (HttpClientErrorException ex){
            StateSingleton.getInstance().setHttpClientErrorException(ex);
        }
    }

    @When("Ik een bestaand account inactief maak")
    public void ikEenBestaandAccountInactiefMaak() throws URISyntaxException {
        URI uri = new URI(baseUrl + "bankaccount/" + StateSingleton.getInstance().getCreated_iban());

        headers.setBearerAuth(employee_jwt);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.DELETE, entity, String.class));

        System.out.println(StateSingleton.getInstance().getResponseEntity().getBody());
    }

    @When("Ik een bestaand en inactief account ophaal")
    public void ikEenBestaandInactiefAccountOphaal() throws URISyntaxException{
        URI uri = new URI(baseUrl + "bankaccount/" + StateSingleton.getInstance().getCreated_iban());

        headers.setBearerAuth(employee_jwt);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.GET, entity, String.class));

        System.out.println(StateSingleton.getInstance().getResponseEntity().getBody());
    }

    @And("Is de status van het account inactive")
    public void isDeStatusVanHetAccountInactive() throws IOException {
        BankAccount account = objectMapper.readValue(StateSingleton.getInstance().getResponseEntity().getBody(), BankAccount.class);
        Assert.assertEquals(Status.INACTIVE, account.getStatus());
    }

    @When("Ik een bestaand account update")
    public void ikEenBestaandAccountUpdate() throws URISyntaxException, JsonProcessingException { //@TODO: Standard account object
        URI uri = new URI(baseUrl + "bankaccount/" + StateSingleton.getInstance().getCreated_iban());

        BankAccount account = new BankAccount();
        account.setIban(StateSingleton.getInstance().getCreated_iban());
        account.setAccountType(AccountType.SAVINGS);
        account.setStatus(Status.ACTIVE);
        account.setUserId(USER_ID);

        headers.setBearerAuth(employee_jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(account), headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.PUT, entity, String.class));

        System.out.println(StateSingleton.getInstance().getResponseEntity().getBody());
    }

    @When("Ik een niet bestaand account update")
    public void ikEenNietBestaandAccountUpdate() throws URISyntaxException, JsonProcessingException {
        URI uri = new URI(baseUrl + "bankaccount/" + UNUSED_IBAN);

        BankAccount account = new BankAccount();
        account.setAccountType(AccountType.REGULAR);
        account.setStatus(Status.ACTIVE);
        account.setUserId(USER_ID);

        headers.setBearerAuth(employee_jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try{
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(account), headers);
            StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.PUT, entity, String.class));
        } catch (HttpClientErrorException ex){
            StateSingleton.getInstance().setHttpClientErrorException(ex);
        }
    }
}
