package br.com.pitarelo.api_todo.application.services;

import br.com.pitarelo.api_todo.domain.model.User;
import br.com.pitarelo.api_todo.infraestruture.persistence.UserJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserJPARepository userJPARepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userJPARepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return user.get();
    }

    public User loadUserById(Long id) {
        Optional<User> user = userJPARepository.findById(id);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with this ID");
        }

        return user.get();
    }

    public User loadUserByEmail(String email) {
        Optional<User> user = userJPARepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found on this email");
        }

        return user.get();
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userJPARepository.save(user);
    }
}
