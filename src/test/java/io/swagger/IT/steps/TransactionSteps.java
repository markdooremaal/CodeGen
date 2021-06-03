package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.swagger.model.BankAccount;
import io.swagger.model.StateSingleton;
import io.swagger.model.Transaction;
import io.swagger.model.enums.AccountType;
import io.swagger.model.enums.Status;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//Class that contains the Step Definitions to test the Transaction endpoints
public class TransactionSteps extends Base {

    @When("Ik alle transactions ophaal")
    public void ikAlleTransactionsOphaal() throws URISyntaxException {
        //URI to send the request to
        URI uri = new URI(baseUrl + "transactions");

        //Add token to the request
        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());

        //Create entitiy with headers and send the request, store the response
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.GET, entity, String.class));
    }

    @When("Ik een nieuwe transaction aanmaak")
    public void ikEenNieuweTransactionAanmaak() throws URISyntaxException, JsonProcessingException, HttpClientErrorException, IOException {
        //URI to send the request to
        URI uri = new URI(baseUrl + "transactions");

        //Object to add
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(BANK_IBAN);
        transaction.setAccountTo(StateSingleton.getInstance().getCreatedIban());
        transaction.userPerforming(USER_ID);
        transaction.setAmount(100.0);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(employee_jwt);

        try{
            //Send the request, store results
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(transaction), headers);
            ResponseEntity<String> responseEntity = template.postForEntity(uri, entity, String.class);
            StateSingleton.getInstance().setResponseEntity(responseEntity);

            //Store the created transaction for future usage
            JsonNode rootNode = objectMapper.readValue(responseEntity.getBody(), JsonNode.class);

            StateSingleton.getInstance().setCreatedTransactionId(rootNode.get("id").intValue());
        } catch (HttpClientErrorException ex){
            StateSingleton.getInstance().setHttpClientErrorException(ex);
        }
    }

    @When("Ik een bestaande transaction ophaal")
    public void ikEenBestaandeTransactionOphaal() throws URISyntaxException{
        //URI to send the request to
        URI uri = new URI(baseUrl + "transaction/" + StateSingleton.getInstance().getCreatedTransactionId());

        //Add headers
        headers.setBearerAuth(employee_jwt);

        //Send the request and store the results
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.GET, entity, String.class));
    }

    @When("Ik een transaction maak van een savings account naar een andere klant")
    public void ikEenTransactionMaakVanEenSavingsAccountNaarEenAndereKlant() throws URISyntaxException, JsonProcessingException, IOException {
        //URI to send the request to
        URI uri = new URI(baseUrl + "transactions");

        //Object to add
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(BANK_IBAN);
        transaction.setAccountTo(StateSingleton.getInstance().getCreatedIban());
        transaction.userPerforming(USER_ID);
        transaction.setAmount(100.0);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(employee_jwt);

        try{
            //Send the request, store results
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(transaction), headers);
            ResponseEntity<String> responseEntity = template.postForEntity(uri, entity, String.class);
            StateSingleton.getInstance().setResponseEntity(responseEntity);

            //Store the created transaction for future usage
            JsonNode rootNode = objectMapper.readValue(responseEntity.getBody(), JsonNode.class);

            StateSingleton.getInstance().setCreatedTransactionId(rootNode.get("id").intValue());
        } catch (HttpClientErrorException ex){
            StateSingleton.getInstance().setHttpClientErrorException(ex);
        }
    }

    @When("Ik een transaction maak van een normaal account naar andersmans savings account")
    public void ikEenTransactionMaakVanEenNormaalAccountNaarAndersmansSavingsAccount() {
        
    }

    @When("Ik een transaction aanmaak als user voor andermans account")
    public void ikEenTransactionAanmaakAlsUserVoorAndermansAccount() {
        
    }

    @When("Ik teveel geld probeer over te maken")
    public void ikTeveelGeldProbeerOverTeMaken() {
        
    }

    @When("Ik niet genoeg geld heb")
    public void ikNietGenoegGeldHeb() {
    }

    @When("Ik teveel transacties uitvoer")
    public void ikTeveelTransactiesUitvoer() {
    }
}