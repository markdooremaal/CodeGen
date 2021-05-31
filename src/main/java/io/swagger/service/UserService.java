package io.swagger.service;

import io.swagger.model.ArrayOfUsers;
import io.swagger.model.User;
import io.swagger.model.enums.Role;
import io.swagger.model.enums.Status;
import io.swagger.repository.IUserRepository;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.function.Predicate;

@Service
public class UserService
{
    private final Role ROLE = Role.CUSTOMER;
    private final Status STATUS = Status.ACTIVE;
    private final Double DAY_LIMIT = 5000.00;
    private final Double TRANSACTION_LIMIT = 2500.00;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*//Get all users
    public ArrayOfUsers findAll(){
        return userRepository.findAll();
    }*/

    public ArrayOfUsers findAll(Specification<User> specification){
        List<User> users = userRepository.findAll(specification);
        return new ArrayOfUsers(users);
    }

    //Get a user by id
    public User findById(int id){
        return userRepository.findById(id);
    }

    //Get a user by email
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    //Get a user from a token
    public User findByToken(String token){
        return userRepository.findByEmail(jwtTokenProvider.getUsername(token));
    }

    //Get delete user by id
    public void delete(int id){
        userRepository.deleteById(id);
    }

    //Update a user
    public void update(User user){
        userRepository.save(user);
    }

    //Set a user as inactive
    public void makeInactive(int id){
        User user = findById(id);
        user.setStatus(Status.INACTIVE);
        update(user);
    }

    //Login the user
    public String login(String email, String password){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            User user = userRepository.findByEmail(email);
            return jwtTokenProvider.createToken(email, user.getRole());
        }
        catch(AuthenticationException ex){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username/password invalid");
        }
    }

    //Add user to the database
    public User add(User user){
        if(userRepository.findByEmail(user.getEmail()) == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword())); //TODO: Implement salt

            if(user.getRole() == null)
                user.setRole(ROLE);

            if(user.getStatus() == null)
                user.setStatus(STATUS);

            if(user.getDayLimit() == null)
                user.setDayLimit(DAY_LIMIT);

            if(user.getTransactionLimit() == null)
                user.setTransactionLimit(TRANSACTION_LIMIT);

            userRepository.save(user);
            return user;
        }
        else
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username already in use");
    }
}
