package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.dto.LoginDTO;
import io.swagger.model.StateSingleton;
import io.swagger.model.User;
import io.swagger.model.enums.Status;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//Class that contains the Step Definitions to test the User endpoints
public class UserSteps extends Base{

    @When("Ik alle users ophaal")
    public void ikAlleUsersOphaal() throws URISyntaxException {
        //URI to send the request to
        URI uri = new URI(baseUrl + "users");
        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());

        //Send the request and store the response
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.GET, entity, String.class));
    }

    @When("Ik inlog met {string} {string}")
    public void ikInlogMet(String email, String password) throws URISyntaxException, JsonProcessingException {
        //URI to send the request to
        URI uri = new URI(baseUrl + "login");

        //Create object to send
        LoginDTO loginLoginDTO = new LoginDTO();
        loginLoginDTO.setEmail(email);
        loginLoginDTO.setPassword(password);

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Send the request and store the response
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(loginLoginDTO), headers);
        StateSingleton.getInstance().setResponseEntity(template.postForEntity(uri, entity, String.class));
    }

    @Then("Krijg ik een jwt token")
    public void krijgIkEenJwtToken() throws JSONException {
        //Get the token from the request
        JSONObject jsonToken = new JSONObject(StateSingleton.getInstance().getResponseEntity().getBody());
        String token = jsonToken.getString("token");

        //Make sure the token isset
        Assert.assertNotNull(token);

        //Store the token
        StateSingleton.getInstance().setJwtToken(token);
    }

    @When("Ik een nieuwe user aanmaak")
    @When("Ik een bestaande user aanmaak")
    public void ikEenNieuweUserAanmaak() throws URISyntaxException, JsonProcessingException, HttpClientErrorException {
        //URI to send the request to
        URI uri = new URI(baseUrl + "users");

        //Create object to send
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@doe.com");
        user.setPassword("test");

        //Add headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());

        try{
            //Send the request and store the response
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(user), headers);
            StateSingleton.getInstance().setResponseEntity(template.postForEntity(uri, entity, String.class));
        } catch (HttpClientErrorException ex){
            StateSingleton.getInstance().setHttpClientErrorException(ex);
        }
    }

    @When("Ik een bestaande user inactief maak")
    public void ikEenBestaandeUserInactiefMaak() throws URISyntaxException {
        //URI to send the request to
        URI uri = new URI(baseUrl + "user/" + USER_ID);

        //Add headers
        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());

        //Send the request and store the response
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.DELETE, entity, String.class));
    }

    @When("Ik een bestaande en inactieve user ophaal")
    public void ikEenBestaandeInactieveUserOphaal() throws URISyntaxException{
        //URI to send the request to
        URI uri = new URI(baseUrl + "user/" + USER_ID);

        //Add headers
        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());

        //Send the request and store the response
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.GET, entity, String.class));
    }

    @And("Is de status van de gebruiker inactive")
    public void isDeStatusVanDeGebruikerInactive() throws IOException {
        //Store the retrieved object, and check if it is inactive
        User user = objectMapper.readValue(StateSingleton.getInstance().getResponseEntity().getBody(), User.class);
        Assert.assertEquals(Status.INACTIVE, user.getStatus());
    }

    @When("Ik een bestaande user update")
    public void ikEenBestaandeUserUpdate() throws URISyntaxException, JsonProcessingException { //@TODO: Standard user object
        //URI to send the request to
        URI uri = new URI(baseUrl + "user/" + USER_ID);

        //Create object to send
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@doe.com");
        user.setPassword("test");

        //Add headers
        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Send the request and store the response
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(user), headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.PUT, entity, String.class));
    }

    @When("Ik een niet bestaande user update")
    public void ikEenNietBestaandeUserUpdate() throws URISyntaxException, JsonProcessingException {
        //URI to send the request to
        URI uri = new URI(baseUrl + "user/" + (USER_ID + 9));

        //Create object to send
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@doe.com");
        user.setPassword("test");

        //Add headers
        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        try{
            //Send the request and store the response
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(user), headers);
            StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.PUT, entity, String.class));
        } catch (HttpClientErrorException ex){
            StateSingleton.getInstance().setHttpClientErrorException(ex);
        }
    }
}