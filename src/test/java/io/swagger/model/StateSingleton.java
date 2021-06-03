package io.swagger.model;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

//@TODO: Implement a world
public class StateSingleton {
    private static StateSingleton instance = new StateSingleton();

    private StateSingleton(){}

    public static StateSingleton getInstance(){
        if(instance == null)
            instance = new StateSingleton();

        return instance;
    }

    private String jwtToken;
    private String created_iban;
    private HttpClientErrorException httpClientErrorException;
    private ResponseEntity<String> responseEntity;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getCreated_iban() {
        return created_iban;
    }

    public void setCreated_iban(String created_iban) {
        this.created_iban = created_iban;
    }

    public HttpClientErrorException getHttpClientErrorException() {
        return httpClientErrorException;
    }

    public void setHttpClientErrorException(HttpClientErrorException httpClientErrorException) {
        this.httpClientErrorException = httpClientErrorException;
    }

    public ResponseEntity<String> getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ResponseEntity<String> responseEntity) {
        this.responseEntity = responseEntity;
    }
}
