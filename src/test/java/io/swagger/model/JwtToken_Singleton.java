package io.swagger.model;

import org.springframework.web.client.HttpClientErrorException;

//@TODO: Implement a world
public class JwtToken_Singleton {
    private static JwtToken_Singleton instance = new JwtToken_Singleton();

    private JwtToken_Singleton(){}

    public static JwtToken_Singleton getInstance(){
        if(instance == null)
            instance = new JwtToken_Singleton();

        return instance;
    }

    private String jwtToken;
    private HttpClientErrorException httpClientErrorException;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public HttpClientErrorException getHttpClientErrorException() {
        return httpClientErrorException;
    }

    public void setHttpClientErrorException(HttpClientErrorException httpClientErrorException) {
        this.httpClientErrorException = httpClientErrorException;
    }
}
