package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.Body;
import io.swagger.model.JwtToken_Singleton;
import io.swagger.model.User;
import io.swagger.security.JwtTokenProvider;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class UserSteps {

    private HttpHeaders headers = new HttpHeaders();
    private String baseUrl = "http://localhost:8080/";
    private RestTemplate template = new RestTemplate();
    private ResponseEntity<String> responseEntity;
    private ObjectMapper objectMapper = new ObjectMapper();

    @When("Ik alle users ophaal")
    public void ikAlleUsersOphaal() throws URISyntaxException {
        URI uri = new URI(baseUrl + "users");
        headers.setBearerAuth(JwtToken_Singleton.getInstance().getJwtToken());

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @Then("Is de status van het request {int}")
    public void isDeStatusVanHetRequest(int expected) {
        int response = responseEntity.getStatusCodeValue();
        Assert.assertEquals(expected, response);
    }

    @When("Ik inlog met {string} {string}")
    public void ikInlogMet(String email, String password) throws URISyntaxException, JsonProcessingException {
        URI uri = new URI(baseUrl + "login");
        Body loginBody = new Body();
        loginBody.setEmail(email);
        loginBody.setPassword(password);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(loginBody), headers);
        responseEntity = template.postForEntity(uri, entity, String.class);
    }

    @Then("Krijg ik een jwt token")
    public void krijgIkEenJwtToken() throws JSONException {
        JSONObject jsonToken = new JSONObject(responseEntity.getBody());
        String token = jsonToken.getString("token");

        Assert.assertNotNull(token);

        JwtToken_Singleton.getInstance().setJwtToken(token);
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
        headers.setBearerAuth(JwtToken_Singleton.getInstance().getJwtToken());

        try{
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(user), headers);
            responseEntity = template.postForEntity(uri, entity, String.class);
        } catch (HttpClientErrorException ex){
            JwtToken_Singleton.getInstance().setHttpClientErrorException(ex);
        }
    }

    @Then("Krijg ik een error {int}")
    public void krijgIkEenError(int statusCode) {
        Assert.assertEquals(statusCode, JwtToken_Singleton.getInstance().getHttpClientErrorException().getRawStatusCode());
    }
}
