package mg.tommy.springboot.springbootwebapp.service.library;

import mg.tommy.springboot.springbootwebapp.model.domain.persistent.Role;
import mg.tommy.springboot.springbootwebapp.model.domain.persistent.User;
import mg.tommy.springboot.springbootwebapp.repository.persistent.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Note that once you define a bean that implements UserDetailsService,
 * Spring won't autoconfigure a default user/password for you
 * via UserDetailsServiceAutoConfiguration
 * (Check the @Conditional annotation on this class to get all the requirements for this autoconfiguration to be effective)
 */
@Profile("!http-basic")
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username '" + username + "' not found");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(Role::getRole).toArray(String[]::new))
                .disabled(false)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    };
}
