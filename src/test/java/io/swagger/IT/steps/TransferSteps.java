package io.swagger.IT.steps;

import com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.java.en.When;
import io.swagger.model.StateSingleton;
import io.swagger.model.Transaction;
import io.swagger.model.Transfer;
import io.swagger.model.enums.Type;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//Class that contains the Step Definitions to test the Transaction endpoints
public class TransferSteps extends Base {

    @When("Ik alle transfers ophaal")
    public void ikAlleTransfersOphaal() throws URISyntaxException {
        //URI to send the request to
        URI uri = new URI(baseUrl + "transfers");

        //Add token to the request
        headers.setBearerAuth(CUSTOMER_JWT);

        //Create entity with headers and send the request, store the response
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.GET, entity, String.class));
    }


    @When("Ik een deposit transfer aanmaak van {double}")
    public void ikEenDepositTransferAanmaak(double amount) throws IOException, URISyntaxException {
        //Object to add
        Transfer transfer = new Transfer();
        transfer.setAccount(REGULAR1_IBAN);
        transfer.setAmount(amount);
        transfer.setType(Type.DEPOSIT);
        transfer.setUserPerforming(USER1);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(CUSTOMER_JWT);

        makeTransfer(transfer);

        //Store the created id to retrieve later
        JsonNode rootNode = objectMapper.readValue(StateSingleton.getInstance().getResponseEntity().getBody(), JsonNode.class);
        StateSingleton.getInstance().setCreatedTransferId(rootNode.get("id").intValue());
    }


    @When("Ik een bestaande transfer ophaal")
    public void ikEenBestaandeTransferOphaal() throws URISyntaxException{
        //URI to send the request to
        URI uri = new URI(baseUrl + "transfer/" + StateSingleton.getInstance().getCreatedTransferId());

        //Add headers
        headers.setBearerAuth(CUSTOMER_JWT);

        //Send the request and store the results
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.GET, entity, String.class));
    }

    @When("Ik een transfer aanmaak als user voor andermans account")
    public void ikEenTransferAanmaakAlsUserVoorAndermansAccount() throws URISyntaxException, IOException{
        //Object to add
        Transfer transfer = new Transfer();
        transfer.setAccount(REGULAR2_IBAN);
        transfer.setAmount(10.0);
        transfer.setType(Type.WITHDRAWAL);
        transfer.setUserPerforming(USER2);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(CUSTOMER_JWT);

        makeTransfer(transfer);
    }

    @When("Ik niet genoeg geld heb voor een transfer")
    public void ikNietGenoegGeldHebVoorEenTransfer() throws IOException, URISyntaxException{
        //Object to add
        Transfer transfer = new Transfer();
        transfer.setAccount(REGULAR1_IBAN);
        transfer.setAmount(10000.0);
        transfer.setType(Type.WITHDRAWAL);
        transfer.setUserPerforming(USER1);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(EMPLOYEE_JWT);

        makeTransfer(transfer);
    }

    @When("Ik een transfer aanmaak vanaf een inactief account")
    public void ikEenTransferAanmaakVanafEenInactiefAccount() throws IOException, URISyntaxException{
        //Object to add
        Transfer transfer = new Transfer();
        transfer.setAccount(INACTIVE1_IBAN);
        transfer.setAmount(10.0);
        transfer.setType(Type.DEPOSIT);
        transfer.setUserPerforming(USER1);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(CUSTOMER_JWT);

        makeTransfer(transfer);
    }

    @When("Ik een transfer maak van een savings account")
    public void ikEenTransferMaakVanEenSavingsAccount()  throws IOException, URISyntaxException {
        //Object to add
        Transfer transfer = new Transfer();
        transfer.setAccount(SAVINGS1_IBAN);
        transfer.setAmount(10.0);
        transfer.setType(Type.WITHDRAWAL);
        transfer.setUserPerforming(USER1);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(CUSTOMER_JWT);

        makeTransfer(transfer);
    }

    @When("Ik teveel geld probeer op te nemen")
    public void ikTeveelGeldProbeerOpTeNemen() throws IOException, URISyntaxException {
        //Object to add
        Transfer transfer = new Transfer();
        transfer.setAccount(REGULAR1_IBAN);
        transfer.setType(Type.WITHDRAWAL);
        transfer.userPerforming(USER1);
        transfer.setAmount(9900.0);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(EMPLOYEE_JWT);

        makeTransfer(transfer);
    }

    @When("Ik te veel hoge transfers uitvoer")
    public void ikTeVeelHogeTransfersUitvoer()  throws IOException, URISyntaxException {
        //Object to add
        Transfer transfer = new Transfer();
        transfer.setAccount(REGULAR1_IBAN);
        transfer.setType(Type.WITHDRAWAL);
        transfer.userPerforming(USER1);
        transfer.setAmount(900.0);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(EMPLOYEE_JWT);

        makeTransfer(transfer);
    }

    //Function to create the transfer in the db
    private void makeTransfer(Transfer transfer) throws IOException, URISyntaxException {
        try{
            //URI to send the request to
            URI uri = new URI(baseUrl + "transfers");

            //Send the request, store results
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(transfer), headers);
            ResponseEntity<String> responseEntity = template.postForEntity(uri, entity, String.class);
            StateSingleton.getInstance().setResponseEntity(responseEntity);
        } catch (HttpClientErrorException ex){
            StateSingleton.getInstance().setHttpClientErrorException(ex);
        }
    }
}