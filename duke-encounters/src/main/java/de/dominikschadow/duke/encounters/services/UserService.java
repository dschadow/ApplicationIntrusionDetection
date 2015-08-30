package de.dominikschadow.duke.encounters.services;

import de.dominikschadow.duke.encounters.domain.User;
import de.dominikschadow.duke.encounters.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Dominik Schadow
 */
@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User register) {
        return null;
    }
}
