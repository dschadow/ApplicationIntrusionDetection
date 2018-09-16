package de.dominikschadow.dukeencounters.config;

import de.dominikschadow.dukeencounters.user.User;
import de.dominikschadow.dukeencounters.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRepositoryUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);

        if (user != null) {
            return user;
        }

        throw new UsernameNotFoundException("User '" + username + "' not found");
    }
}
