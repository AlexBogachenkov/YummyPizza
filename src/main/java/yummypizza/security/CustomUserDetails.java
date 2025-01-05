package yummypizza.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import yummypizza.core.domain.UserRole;

import java.util.Collection;

@Getter
@Setter
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private UserRole role;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public CustomUserDetails(String email, String password, Collection<? extends GrantedAuthority> authorities,
                             Long id, String firstName, String lastName, String phone, UserRole role) {
        super(email, password, authorities);
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
    }

}
