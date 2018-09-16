package de.dominikschadow.dukeencounters.registration;

import de.dominikschadow.dukeencounters.encounter.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegistrationForm {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder().firstname(firstname).lastname(lastname).username(username).email(email).password(passwordEncoder.encode(password)).build();
    }
}
