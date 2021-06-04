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
    protected final int USER1 = 1;
    protected final int USER2 = 2;
    protected final String UNUSED_IBAN = "nl31inho9492452917";
    protected final String BANK_IBAN = "nl01inho0000000001";
    protected final String REGULAR1_IBAN = "nl01inho1100000001";
    protected final String REGULAR2_IBAN = "nl01inho3300000001";
    protected final String SAVINGS1_IBAN = "nl01inho2200000001";
    protected final String SAVINGS2_IBAN = "nl01inho4400000001";
    protected final String INACTIVE1_IBAN = "nl01inho5500000001";

    protected final String EMPLOYEE_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXJrQGdtYWlsLmNvbSIsImF1dGgiOiJlbXBsb3llZSIsImlhdCI6MTYyMjcxNjY5NSwiZXhwIjoxNjMyNzE2Njk1fQ.Qp8J4MwIbSsYUuMrlEPJZU9VCVKmnQk1dnIJ0u1HPpo";
}
