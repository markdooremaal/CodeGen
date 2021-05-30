package io.swagger.api;

import io.swagger.model.dto.LoginDTO;
import io.swagger.model.dto.LoginResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-25T09:30:53.687Z[GMT]")
@RestController
public class LoginApiController implements LoginApi {

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(LoginApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public LoginApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<LoginResponseDTO> login(@Parameter(in = ParameterIn.DEFAULT, description = "Username and password", required=true, schema=@Schema()) @Valid @RequestBody LoginDTO loginDTO) {
        String accept = request.getHeader("Accept");
        if (accept != null && request.getContentType().equals("application/json")) {
            try {
                String jwtToken = userService.login(loginDTO.getEmail(), loginDTO.getPassword());
                return new ResponseEntity<LoginResponseDTO>(objectMapper.readValue("{\n  \"token\" : \"" + jwtToken + "\"\n}", LoginResponseDTO.class), HttpStatus.OK);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<LoginResponseDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<LoginResponseDTO>(HttpStatus.NOT_IMPLEMENTED);
    }

}
