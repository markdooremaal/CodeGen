package io.swagger.IT.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

//Class with values used through different Step Definitions
public class Base {
    protected HttpHeaders headers = new HttpHeaders();
    protected String baseUrl = "http://localhost:8080/";
    protected RestTemplate template = new RestTemplate();
    protected ObjectMapper objectMapper = new ObjectMapper();
    protected final int USER_ID = 3;
    protected final String UNUSED_IBAN = "nl31inho9492452917";
    protected final String employee_jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXJrQGdtYWlsLmNvbSIsImF1dGgiOiJlbXBsb3llZSIsImlhdCI6MTYyMjcxNjY5NSwiZXhwIjoxNjMyNzE2Njk1fQ.Qp8J4MwIbSsYUuMrlEPJZU9VCVKmnQk1dnIJ0u1HPpo";
}
