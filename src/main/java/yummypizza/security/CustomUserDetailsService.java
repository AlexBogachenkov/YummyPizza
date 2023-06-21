package yummypizza.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.User;
import yummypizza.core.domain.UserRole;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalOfUser = userRepository.findByEmail(email);
        User user = optionalOfUser.orElse(null);

        if (user == null) {
//            return new org.springframework.security.core.userdetails.User(
//                    " ", " ", List.of());
            return new CustomUserDetails(" ", " ", List.of());
        }
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(), user.getPassword(), getAuthority(user.getRole()));
        return new CustomUserDetails(user.getEmail(), user.getPassword(), getAuthority(user.getRole()),
                user.getId(), user.getFirstName(), user.getLastName(), user.getPhone(), user.getRole());
    }

    private Collection<? extends GrantedAuthority> getAuthority(UserRole userRole) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userRole.toString()));
    }

}
