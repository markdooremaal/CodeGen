package io.swagger.model;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

public class StateSingleton {
    private static StateSingleton instance = new StateSingleton();

    private StateSingleton(){}

    public static StateSingleton getInstance(){
        if(instance == null)
            instance = new StateSingleton();

        return instance;
    }

    private String createdIban;
    private HttpClientErrorException httpClientErrorException;
    private ResponseEntity<String> responseEntity;
    private int createdTransactionId;
    private int createdTransferId;

    public String getCreatedIban() {
        return createdIban;
    }

    public int getCreatedTransactionId() {
        return createdTransactionId;
    }

    public void setCreatedTransactionId(int createdTransactionId) {
        this.createdTransactionId = createdTransactionId;
    }

    public void setCreatedIban(String createdIban) {
        this.createdIban = createdIban;
    }

    public int getCreatedTransferId() {
        return createdTransferId;
    }

    public void setCreatedTransferId(int createdTransferId) {
        this.createdTransferId = createdTransferId;
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
