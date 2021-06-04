package io.swagger.IT.steps;

import com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.java.en.When;
import io.swagger.model.StateSingleton;
import io.swagger.model.Transaction;
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


    @When("Ik een transactie aanmaak van een regular account naar een andere klant")
    public void ikEenTransactieAanmaakVanEenRegularAccountNaarEenAndereKlant() throws IOException, URISyntaxException {
        //Object to add
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(REGULAR1_IBAN);
        transaction.setAccountTo(REGULAR2_IBAN);
        transaction.userPerforming(USER1);
        transaction.setAmount(10.0);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(EMPLOYEE_JWT);

        makeTransaction(transaction);

        //STore the created id to retrieve later
        JsonNode rootNode = objectMapper.readValue(StateSingleton.getInstance().getResponseEntity().getBody(), JsonNode.class);
        StateSingleton.getInstance().setCreatedTransactionId(rootNode.get("id").intValue());
    }


    @When("Ik een bestaande transaction ophaal")
    public void ikEenBestaandeTransactionOphaal() throws URISyntaxException{
        //URI to send the request to
        URI uri = new URI(baseUrl + "transaction/" + StateSingleton.getInstance().getCreatedTransactionId());

        //Add headers
        headers.setBearerAuth(EMPLOYEE_JWT);

        //Send the request and store the results
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.GET, entity, String.class));
    }

    @When("Ik een transaction maak van een savings account naar een andere klant")
    public void ikEenTransactionMaakVanEenSavingsAccountNaarEenAndereKlant() throws IOException, URISyntaxException {
        //Object to add
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(SAVINGS1_IBAN);
        transaction.setAccountTo(REGULAR2_IBAN);
        transaction.userPerforming(USER1);
        transaction.setAmount(10.0);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(EMPLOYEE_JWT);

        makeTransaction(transaction);
    }

    @When("Ik een transaction maak van een normaal account naar andersmans savings account")
    public void ikEenTransactionMaakVanEenNormaalAccountNaarAndersmansSavingsAccount() throws URISyntaxException, IOException {
        //Object to add
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(REGULAR1_IBAN);
        transaction.setAccountTo(SAVINGS2_IBAN);
        transaction.userPerforming(USER1);
        transaction.setAmount(10.0);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(EMPLOYEE_JWT);

        makeTransaction(transaction);
    }

    @When("Ik een transaction aanmaak als user voor andermans account")
    public void ikEenTransactionAanmaakAlsUserVoorAndermansAccount() throws URISyntaxException, IOException{
        //Object to add
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(REGULAR2_IBAN);
        transaction.setAccountTo(REGULAR1_IBAN);
        transaction.userPerforming(USER1); //@TODO REmove
        transaction.setAmount(10.0);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());

        makeTransaction(transaction);
    }

    @When("Ik teveel geld probeer over te maken")
    public void ikTeveelGeldProbeerOverTeMaken() throws IOException, URISyntaxException{
        //Object to add
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(REGULAR1_IBAN);
        transaction.setAccountTo(REGULAR2_IBAN);
        transaction.userPerforming(USER1); //@TODO REmove
        transaction.setAmount(1001.0);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(EMPLOYEE_JWT);

        makeTransaction(transaction);
    }

    @When("Ik niet genoeg geld heb")
    public void ikNietGenoegGeldHeb() throws IOException, URISyntaxException{
        //Object to add
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(REGULAR1_IBAN);
        transaction.setAccountTo(REGULAR2_IBAN);
        transaction.userPerforming(USER1); //@TODO REmove
        transaction.setAmount(990.0);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(EMPLOYEE_JWT);

        makeTransaction(transaction);
    }

    @When("Ik te veel hoge transacties uitvoer")
    public void ikTeVeelHogeTransactiesUitvoer() throws IOException, URISyntaxException{
        //Object to add
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(REGULAR1_IBAN);
        transaction.setAccountTo(REGULAR2_IBAN);
        transaction.userPerforming(USER1); //@TODO REmove
        transaction.setAmount(99.0);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(EMPLOYEE_JWT);

        makeTransaction(transaction);
    }

    @When("Ik een transactie aanmaak vanaf een inactief account")
    public void ikEenTransactieAanmaakVanafEenInactiefAccount() throws IOException, URISyntaxException{
        //Object to add
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(INACTIVE1_IBAN);
        transaction.setAccountTo(REGULAR2_IBAN);
        transaction.userPerforming(USER1); //@TODO REmove
        transaction.setAmount(10.0);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(EMPLOYEE_JWT);

        makeTransaction(transaction);
    }

    private void makeTransaction(Transaction transaction) throws IOException, URISyntaxException {
        try{
            //URI to send the request to
            URI uri = new URI(baseUrl + "transactions");

            //Send the request, store results
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(transaction), headers);
            ResponseEntity<String> responseEntity = template.postForEntity(uri, entity, String.class);
            StateSingleton.getInstance().setResponseEntity(responseEntity);
        } catch (HttpClientErrorException ex){
            StateSingleton.getInstance().setHttpClientErrorException(ex);
        }
    }
}