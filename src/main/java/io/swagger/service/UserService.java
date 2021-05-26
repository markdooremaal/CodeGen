package io.swagger.service;

import io.swagger.model.ArrayOfUsers;
import io.swagger.model.User;
import io.swagger.model.enums.Status;
import io.swagger.repository.IUserRepository;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService
{
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Get all users
    public ArrayOfUsers findAll(){
        return userRepository.findAll();
    }

    //Get a user by id
    public User findById(int id){
        return userRepository.findById(id);
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
            userRepository.save(user);
            return user;
        }
        else
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username already in use");
    }
}
