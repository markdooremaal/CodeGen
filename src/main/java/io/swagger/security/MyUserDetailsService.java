package io.swagger.security;

import io.swagger.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        io.swagger.model.User user = userRepository.findByEmail(s);

        if(user == null)
            throw new UsernameNotFoundException("User " + s + " not found");

        return User.withUsername(s)
                .password(user.getPassword())
                .authorities(user.getRole())
                .accountExpired(false)
                .accountLocked((false))
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
