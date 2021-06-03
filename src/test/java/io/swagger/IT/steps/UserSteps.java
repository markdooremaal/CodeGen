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

public class UserSteps extends Base{

    @When("Ik alle users ophaal")
    public void ikAlleUsersOphaal() throws URISyntaxException {
        URI uri = new URI(baseUrl + "users");
        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.GET, entity, String.class));
    }

    @When("Ik inlog met {string} {string}")
    public void ikInlogMet(String email, String password) throws URISyntaxException, JsonProcessingException {
        URI uri = new URI(baseUrl + "login");
        LoginDTO loginLoginDTO = new LoginDTO();
        loginLoginDTO.setEmail(email);
        loginLoginDTO.setPassword(password);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(loginLoginDTO), headers);
        StateSingleton.getInstance().setResponseEntity(template.postForEntity(uri, entity, String.class));
    }

    @Then("Krijg ik een jwt token")
    public void krijgIkEenJwtToken() throws JSONException {
        JSONObject jsonToken = new JSONObject(StateSingleton.getInstance().getResponseEntity().getBody());
        String token = jsonToken.getString("token");

        Assert.assertNotNull(token);

        //if()

        StateSingleton.getInstance().setJwtToken(token);
    }

    @When("Ik een nieuwe user aanmaak")
    @When("Ik een bestaande user aanmaak")
    public void ikEenNieuweUserAanmaak() throws URISyntaxException, JsonProcessingException, HttpClientErrorException {
        URI uri = new URI(baseUrl + "users");

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@doe.com");
        user.setPassword("test");

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());

        try{
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(user), headers);
            StateSingleton.getInstance().setResponseEntity(template.postForEntity(uri, entity, String.class));
        } catch (HttpClientErrorException ex){
            StateSingleton.getInstance().setHttpClientErrorException(ex);
        }
    }

    @When("Ik een bestaande user inactief maak")
    public void ikEenBestaandeUserInactiefMaak() throws URISyntaxException {
        URI uri = new URI(baseUrl + "user/" + USER_ID);

        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.DELETE, entity, String.class));
    }

    @When("Ik een bestaande en inactieve user ophaal")
    public void ikEenBestaandeInactieveUserOphaal() throws URISyntaxException{
        URI uri = new URI(baseUrl + "user/" + USER_ID);

        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.GET, entity, String.class));
    }

    @And("Is de status van de gebruiker inactive")
    public void isDeStatusVanDeGebruikerInactive() throws IOException {
        User user = objectMapper.readValue(StateSingleton.getInstance().getResponseEntity().getBody(), User.class);
        Assert.assertEquals(Status.INACTIVE, user.getStatus());
    }

    @When("Ik een bestaande user update")
    public void ikEenBestaandeUserUpdate() throws URISyntaxException, JsonProcessingException { //@TODO: Standard user object
        URI uri = new URI(baseUrl + "user/" + USER_ID);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@doe.com");
        user.setPassword("test");

        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(user), headers);
        StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.PUT, entity, String.class));
    }

    @When("Ik een niet bestaande user update")
    public void ikEenNietBestaandeUserUpdate() throws URISyntaxException, JsonProcessingException {
        URI uri = new URI(baseUrl + "user/" + (USER_ID + 9));

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@doe.com");
        user.setPassword("test");

        headers.setBearerAuth(StateSingleton.getInstance().getJwtToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        try{
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(user), headers);
            StateSingleton.getInstance().setResponseEntity(template.exchange(uri, HttpMethod.PUT, entity, String.class));
        } catch (HttpClientErrorException ex){
            StateSingleton.getInstance().setHttpClientErrorException(ex);
        }
    }
}
